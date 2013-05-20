#!/usr/bin/perl

use Data::Dumper;
use File::Basename;
my %files = ();

$Data::Dumper::Sortkeys = 1;

foreach my $rpm ( glob("/itch/opennms-dev.git/target/rpm/RPMS/noarch/opennms*rpm") ) {
  my ($rpmfile) = fileparse($rpm);
  open(LIST, "rpm -qlp $rpm|");
  while(my $path = <LIST>) {
    next if ($path !~ m|\.jar$|);
    next if ($path =~ m|1\.13\.0\-SNAPSHOT|);
    chomp $path;
    my($filename, $directories, $suffix) = fileparse($path);
#    printf "%s/%s\n", $directories, $filename;
    if ($filename =~ /^(\S+)\-(\d+\.\d+(\.\d+)*(\.(RELEASE|GA|v?\d{8}|v\d{12}|Final)|\-(v?\d{8}|(alpha|beta)\-\d+|SNAPSHOT))?)\.jar$/) {
      push @{ $files{$1}{$2}}, $rpmfile.":".$directories;
      next;
    }
#    if ($filename =~ /^(\S+)\-(\d+\.\d+\.\d+)\.jar$/) {
#      push @{ $files{$1}{$2}}, $directories;
#      next;
#    }
    push @{$files{ $filename }{'unknown'}}, $rpmfile.":".$directories;
  }
  close(LIST);
}

if(0) {
open(LIST, "find /opt/opennms/ -name '*.jar' |");
while(my $path = <LIST>) {
  next if ($path =~ m|1\.13\.0\-SNAPSHOT|);
  chomp $path;
  my($filename, $directories, $suffix) = fileparse($path);
#  printf "%s/%s\n", $directories, $filename;
  if ($filename =~ /^(\S+)\-(\d+\.\d+(\.\d+)*(\.(RELEASE|GA|v?\d{8}|v\d{12}|Final)|\-(v?\d{8}|(alpha|beta)\-\d+|SNAPSHOT))?)\.jar$/) {
    push @{ $files{$1}{$2}}, $directories;
    next;
  }
#  if ($filename =~ /^(\S+)\-(\d+\.\d+\.\d+)\.jar$/) {
#    push @{ $files{$1}{$2}}, $directories;
#    next;
#  }
  push @{$files{ $filename }{'unknown'}}, $directories;
}
close(LIST);
}

foreach my $lib (sort keys %files) {
  if (scalar(keys %{ $files{$lib} }) > 1) {
    foreach my $version (sort keys %{ $files{$lib} }) {
      foreach my $directories (@{ $files{$lib}{$version} }) {
        printf("%30s\t%20s\t%s\n", $lib, $version, $directories);
      }
    }
  }
}

#print Data::Dumper->Dump([\%files], [qw(*files)]);
