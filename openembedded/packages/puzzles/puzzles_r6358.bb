
DEPENDS = "gtk+"
PR = "r1"

SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz"

CFLAGS_prepend = " -I./ `${STAGING_BINDIR}/pkg-config gtk+-2.0 --cflags` "
do_compile_prepend = " export 'XLDFLAGS=${LDFLAGS} `${STAGING_BINDIR}/pkg-config gtk+-2.0 --libs`'; "

PACKAGES = "${PN} ${PN}-desktop"

FILES_${PN} = "${prefix}/games/*"
FILES_${PN}-desktop = "${datadir}/applications/*"

do_compile () {
	CFLAGS=" -I./ `${STAGING_BINDIR}/pkg-config gtk+-2.0 --cflags` " oe_runmake
}

do_install () {
    export prefix=${D}
    export DESTDIR=${D}
    install -d ${D}/${prefix}
    install -d ${D}/${prefix}/games
    oe_runmake install
    
    install -d ${D}/${datadir}
    install -d ${D}/${datadir}/applications
    cd ${D}/${prefix}/games 

    for prog in *; do
	if [ -x $prog ]; then
	    echo "making ${D}/${datadir}/applications/$prog.desktop"
	    cat <<STOP > ${D}/${datadir}/applications/$prog.desktop
[Desktop Entry]
Encoding=UTF-8
Name=$prog
Exec=${prefix}/games/$prog
Icon=game.png
Terminal=false
Type=Application
Categories=Game
StartupNotify=true
STOP
        fi
    done
}
