#!/usr/bin/perl -w

/* Renata Ghisloti Duarte de Souza */


# Preprocessing of ratings.dat file from Grouplens.
# Format of ratings.dat file: Userid::Itemid::Rating::Timestamp
# There are 6000 users and 4000 movies.
# This script divides the set in two parts. Set1
# will be used for recommendation and Set2 can be used
# to verify the result.

open (FILE, "<", "ratings.dat");
open (FH1,'>', "ratings_set1.dat");
open (FH2, '>', "ratings_set2.dat");

# initial userid is 1
my $current_user = 1;
my $count = 0;

while(<FILE>){
    my @temp = split(/::/, $_);
   
    if($count <= 5){
        print(FH2 $temp[0].','.$temp[1].','.$temp[2]."\n");
    }
    else {
        print(FH1 $temp[0].','.$temp[1].','.$temp[2]."\n");
    }

    $count ++;

    if($temp[0] != $current_user) {
        $count = 0;
        $current_user = $temp[0];
    }
}

close FH1 or die $!;
close FH2 or die $!;
close FILE or die $!;

