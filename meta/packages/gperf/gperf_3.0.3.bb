DESCRIPTION = "GNU gperf is a perfect hash function generator"
HOMEPAGE = "http://www.gnu.org/software/gperf"
SUMMARY  = "Generate a perfect hash function from a set of keywords"
# 3.0.4 change to GPLv3, but only native version is used
LICENSE  = "GPLv2+"

SRC_URI  = "${GNU_MIRROR}/gperf/gperf-${PV}.tar.gz \
            file://autoreconf.patch"

inherit autotools

PR = "r1"

BBCLASSEXTEND = "native"
