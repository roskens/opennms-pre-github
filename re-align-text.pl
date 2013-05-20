#!/usr/bin/perl
#

my $file = shift;
my $gidlen = 0;
my $endlen = 0;

open(IN, "<", $file);
while(<IN>) {
  if (/^\[(\S+)\](\s+)(.*?)\s\.\.\.$/) {
    my $level = $1;
    my $spacing = $2;
    my $gidaid = $3;
    if ($gidlen < length($gidaid)) { $gidlen = length($gidaid); }
    my $line = <IN>;
    chomp $line;
    $line =~ s/^\[$level\]\s+//;
    if ($endlen < length($line)) { $endlen = length($line); }
#    print;
  }
}
close(IN);

open(IN, "<", $file);
while(<IN>) {
  if (/^\[(\S+)\](\s+)(.*?)\s\.\.\.$/) {
    my $level = $1;
    my $spacing = $2;
    my $gidaid = $3;
    my $line = <IN>;
    chomp $line;
    $line =~ s/^\[$level\]\s+//;
    my $end = "."x($gidlen+$endlen-length($gidaid)-length($line));
    printf "[%s]%s%s %s %s\n", $level, $spacing, $gidaid, $end, $line;
    next;
  }
  if (/^\[(\S+)\](\s+)(.*?)\s\.+\s(.*)$/) {
    my $level = $1;
    my $spacing = $2;
    my $gidaid = $3;
    my $line = $4;
    my $end = "."x($gidlen+$endlen-length($gidaid)-length($line));
    printf "[%s]%s%s %s %s\n", $level, $spacing, $gidaid, $end, $line;
    next;
  }
  if (/^\[(\S+)\](\s+)(.*?)\s+(SUCCESS\s.*)$/) {
    my $level = $1;
    my $spacing = $2;
    my $gidaid = $3;
    my $line = $4;
    my $end = "."x($gidlen+$endlen-length($gidaid)-length($line));
    printf "[%s]%s%s %s %s\n", $level, $spacing, $gidaid, $end, $line;
    next;
  }
  print;
}
close(IN);
