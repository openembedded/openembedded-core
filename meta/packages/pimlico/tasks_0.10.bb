require tasks.inc

PR="r2"

SRC_URI = "http://pimlico-project.org/sources/${PN}/${PN}-${PV}.tar.gz \
        file://tasks-single.diff;patch=1 \
        file://delete-crash.diff;patch=1;pnum=0 \
        file://tasks-owl.diff;patch=1;pnum=0"
