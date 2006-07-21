
DEPENDS = "gtk+"
PR = "r2"

SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz \
           file://game.png"

do_compile_prepend = " \
        export XLDFLAGS='${LDFLAGS} `${STAGING_BINDIR}/pkg-config gtk+-2.0 --libs`'; \
	export CFLAGS='${CFLAGS} -I./ `${STAGING_BINDIR}/pkg-config gtk+-2.0 --cflags`'; "

FILES_${PN} = "${prefix}/games/* ${datadir}/applications/* ${datadir}/pixmaps"

do_install () {
    export prefix=${D}
    export DESTDIR=${D}
    install -d ${D}/${prefix}
    install -d ${D}/${prefix}/games
    oe_runmake install
    
    install -d ${D}/${datadir}
    install -d ${D}/${datadir}/applications
    install -d ${D}/${datadir}/pixmaps

    install ${WORKDIR}/game.png ${D}/${datadir}/pixmaps

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
SingleInstance=true
STOP
        fi
    done
}
