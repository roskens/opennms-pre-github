/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.jrobin.core.RrdDb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.opennms.netmgt.rrd.RrdDataSource;
import org.opennms.netmgt.rrd.newts.NewtsRrdDefinition;
import org.opennms.netmgt.rrd.newts.NewtsMetric;
import org.opennms.netmgt.rrd.newts.NewtsRrd;
import org.opennms.netmgt.rrd.newts.NewtsRrdStrategy;
import org.opennms.newts.api.Sample;
import org.opennms.newts.api.Timestamp;
import org.opennms.newts.api.ValueType;
import org.jrobin.core.ArcDef;
import org.jrobin.core.Archive;
import org.jrobin.core.DsDef;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;

public final class RrdToNewts implements Runnable {

    /**
     * JRobin to RRD converter
     */
    private final RrdToNewtsConverter m_jrbToRrdConverter;
    private final NewtsRrdStrategy m_strategy;

    /**
     * Queue for files to convert
     */
    private final Queue<String> m_queue = new ConcurrentLinkedQueue<>();

    /**
     * Queue closed status
     */
    private boolean queueClosed = false;
    
    /**
     * Default constructor to convert from JRB to XML
     *
     * @param jrbToRrdConverter
     * @param filename
     */
    public RrdToNewts(final RrdToNewtsConverter jrbToRrdConverter, final String filename) {
        m_jrbToRrdConverter = jrbToRrdConverter;
        m_strategy = m_jrbToRrdConverter.m_rrdStrategy;
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("filename was null or empty!");
        }
        add(filename);
    }

    /**
     * <p>Close queue</p>
     * Close queue and set queue closed status
     */
    public void close() {
        queueClosed = true;
    }

    /**
     * <p>Add path</p>
     *
     * Add absolute path to queue for thread to convert
     *
     * @param path absolute path to JRobin file to convert as {@link java.lang.String}
     */
    public void add(final String path) {
        m_queue.add(path);
    }

    /**
     * <p>Get RRD DB reference</p>
     *
     * TODO: Why do we need this here?
     * @param path
     * @return
     * @throws IOException
     * @throws RrdException
     */
    private RrdDb getRrdDbReference(final String path) throws IOException, RrdException {
        return new RrdDb(path, true);
    }

    /**
     * <p>Size of the queue</p>
     * Get the size of the queue for files to convert.
     *
     * @return size of queue for files to convert
     */
    public int size() {
        return m_queue.size();
    }

    /**
     * <p>Convert JRobin to XML</p>
     * Export JRobin file to XML file using memory mapped file access.
     *
     * @param path absolute path to JRobin file without file extension as {@link java.lang.String}
     * @throws IOException
     */
    public void convertToNewts(final String path) throws IOException, RrdException {
        System.out.println("Converting "+path);
        File file = new File(path);
        RrdDb rrdDb = getRrdDbReference(path + RrdToNewtsConverter.FILE_TYPE.JRB.ext());
        RrdDef def = rrdDb.getRrdDef();
        //rrdDb.dumpXml(path+Rrd4JToNewtsConverter.FILE_TYPE.XML.ext());

        try {
            int dsCount = def.getDsCount();
            int arcCount = def.getArcCount();
            List<RrdDataSource> dataSources = new ArrayList<>(dsCount);
            List<String> rraList = new ArrayList<>(arcCount);
            for(DsDef ddef : def.getDsDefs()) {
                dataSources.add(new RrdDataSource(ddef.getDsName(), ddef.getDsType(),
                                (int) ddef.getHeartbeat(), doubleToString(ddef.getMinValue()),
                                doubleToString(ddef.getMaxValue())));
            }
            for(ArcDef adef : def.getArcDefs()) {
                if(adef.getConsolFun().equals("AVERAGE")) {
                    rraList.add(adef.dump());
                }
            }
            
            NewtsRrdDefinition ndef = m_strategy.createDefinition("", file.getParent(), file.getName(), (int) def.getStep(), dataSources, rraList);
            m_strategy.createFile(ndef, null);
            NewtsRrd nres = m_strategy.openFile(file.getAbsolutePath());
            List<Sample> samples = new ArrayList<>();
            Timestamp ts;
            
            for (int j = 0; j < dsCount; j++) {
                NewtsMetric metric = nres.getMetric(j);
                System.out.println("metric: "+metric.getName()+"/"+metric.getType());

                long midTime = System.currentTimeMillis() / 1000L;
                System.out.println("midTime: "+midTime);
                 
                for(int i = 0; i < arcCount; i++) {
                    Archive a = rrdDb.getArchive(i);
                    if(!"AVERAGE".equals(a.getConsolFun())) {
                        continue;
                    }
                    long startTime = a.getStartTime();
                    long endTime = a.getEndTime();
                    long step = a.getArcStep();

                    System.out.printf("archive: step=%d, startTime=%d, endTime=%d\n", step, startTime, endTime);
                    double[] values = a.getRobin(j).getValues();

                    for(int k = 0; k < values.length; k++) {
                        long t = startTime + k * step;
                        if (t < midTime) {
                            ts = new Timestamp(t, TimeUnit.SECONDS);

                            samples.add(
                              new Sample(
                                ts,
                                nres.getResource(),
                                metric.getName(),
                                metric.getType(),
                                ValueType.compose(values[k], metric.getType())
                              )
                            );
                        } else {
                            System.out.println("Skipping time="+t+ " < "+midTime);
                            break;
                        }
                        try {
                            if (samples.size() >= 100) {
                                m_strategy.getConnection().insert(samples, true);
                                samples.clear();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!samples.isEmpty()) {
                        m_strategy.getConnection().insert(samples, true);
                        samples.clear();
                    }
                    
                    if (midTime == 0 || midTime > startTime) {
                        System.out.println("midTime: "+midTime+" -> "+startTime);
                        midTime = startTime;
                    }
                }
            }
            
            m_strategy.closeFile(nres);
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            releaseRrdDbReference(rrdDb);
            try {
                final int BUFFER = 2048;
                try (FileOutputStream dest = new FileOutputStream(path+".zip");
                     ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
                     FileInputStream fis = new FileInputStream(path + RrdToNewtsConverter.FILE_TYPE.JRB.ext());
                     BufferedInputStream bis = new BufferedInputStream(fis, BUFFER);) {
                    ZipEntry entry = new ZipEntry(file.getName()+RrdToNewtsConverter.FILE_TYPE.JRB.ext());
                    out.putNextEntry(entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    while((count = bis.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private String doubleToString(final double d) {
        if (Double.isNaN(d)) {
            return "U";
        }
        return Double.toString(d);
    }

    /**
     * <p>Release RRD database reference</p>
     *
     * @param rrdDb
     * @throws IOException
     * @throws RrdException
     */
    private void releaseRrdDbReference(final RrdDb rrdDb) throws IOException {
        rrdDb.close();
    }

    /**
     * <p>Run conversion</p>
     *
     * Run for each file in queue
     */
    @Override
    public void run() {
        while (!m_queue.isEmpty()) {
            try {
                String path = m_queue.poll();

                convertToNewts(path);

                m_jrbToRrdConverter.increaseConvertedFiles();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (RrdException e) {
                e.printStackTrace();
            }
        }
    }
}