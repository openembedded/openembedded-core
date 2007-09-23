require tasks.inc

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
        file://tasks-owl.diff;patch=1;pnum=0"
