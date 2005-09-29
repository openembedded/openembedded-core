
DEPENDS = "gtk+"

SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz"

CFLAGS_prepend = " -I./ `${STAGING_BINDIR}/pkg-config gtk+-2.0 --cflags` "
do_compile_prepend = " export 'XLDFLAGS=${LDFLAGS} `${STAGING_BINDIR}/pkg-config gtk+-2.0 --libs`'; "

FILES_${PN} = "usr/games/*"

do_install () {
    export prefix=${D}
    export DESTDIR=${D}
    install -d ${D}/usr
    install -d ${D}/usr/games
    oe_runmake install 
}
