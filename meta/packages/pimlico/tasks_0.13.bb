require tasks.inc

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
           file://fix_configure.patch;patch=1;status=merged \
           file://tasks-owl.diff;patch=1;pnum=0"

PR = "r1"
