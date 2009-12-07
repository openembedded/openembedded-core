require tasks.inc

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
           file://fix_configure.patch;patch=1;status=merged"

SRC_URI_append_poky = " file://tasks-owl.diff;patch=1 "

PR = "r3"
