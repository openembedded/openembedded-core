require tasks.inc

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
           file://fix_configure.patch;patch=1;status=merged"

PR = "r2"
