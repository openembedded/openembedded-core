HOMEPAGE = "http://www.gnu.org/software/gperf"
LICENSE  = "GPL"
SUMMARY  = "Generate a perfect hash function from a set of keywords"

SRC_URI  = "${GNU_MIRROR}/gperf/gperf-${PV}.tar.gz \
            file://autoreconf.patch;patch=1"

inherit autotools
