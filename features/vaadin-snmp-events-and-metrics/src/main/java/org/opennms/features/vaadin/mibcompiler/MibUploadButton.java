/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2012-2014 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2014 The OpenNMS Group, Inc.
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

package org.opennms.features.vaadin.mibcompiler;

import java.io.OutputStream;

import org.opennms.features.vaadin.api.Logger;

import com.vaadin.ui.Upload;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The Class MIB Upload Button.
 *
 * @author <a href="mailto:agalue@opennms.org">Alejandro Galue</a>
 */
@SuppressWarnings("serial")
public abstract class MibUploadButton extends Upload {

    /**
     * Instantiates a new MIB upload button.
     *
     * @param pendingDir the pending directory
     * @param compiledDir the compiled directory
     * @param logger the logger
     */
    public MibUploadButton(final Path pendingDir, final Path compiledDir, final Logger logger) {

        setCaption(null);
        setImmediate(true);
        setButtonCaption("Upload MIB");

        setReceiver(new Receiver() {
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                Path file = pendingDir.resolve(filename);
                try {
                    return Files.newOutputStream(file);
                } catch (IOException e) {
                    logger.warn("Unable to create file '" + file + "': " + e.getLocalizedMessage());
                    return null;
                }
            }
        });

        addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(StartedEvent event) {
                Path pending = pendingDir.resolve(event.getFilename());
                Path compiled = compiledDir.resolve(event.getFilename());
                if (Files.exists(pending)) {
                    logger.warn("The file " + pending + " already exist on Pending directory.");
                } else if (Files.exists(compiled)) {
                    logger.warn("The file " + compiled + " already exist on Compiled directory.");
                } else {
                    logger.info("Uploading " + event.getFilename());
                }
            }
        });

        addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(FailedEvent event) {
                logger.warn("An error has been found: " + event.getReason() == null? "unknown error" : event.getReason().getLocalizedMessage());
            }
        });

        addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(SucceededEvent event) {
                String mibFilename = event.getFilename();
                logger.info("File " + mibFilename + " successfuly uploaded");
                uploadHandler(mibFilename);
            }
        });
    }

    /**
     * Upload handler.
     *
     * @param filename the filename
     */
    public abstract void uploadHandler(String filename);

}
