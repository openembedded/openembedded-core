
DEPENDS = "gtk+"
PR = "r4"

SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz"

do_compile_prepend = " \
        export XLDFLAGS='${LDFLAGS} `${STAGING_BINDIR_NATIVE}/pkg-config gtk+-2.0 --libs`'; \
	export CFLAGS='${CFLAGS} -I./ `${STAGING_BINDIR_NATIVE}/pkg-config gtk+-2.0 --cflags`'; "

FILES_${PN} = "${prefix}/games/* ${datadir}/applications/*"
FILES_${PN}-dbg += "${prefix}/games/.debug"

do_install () {
    rm -rf ${D}/*
    export prefix=${D}
    export DESTDIR=${D}
    install -d ${D}/${prefix}/
    install -d ${D}/${prefix}/games/
    oe_runmake install
    
    install -d ${D}/${datadir}/
    install -d ${D}/${datadir}/applications/

    cd ${D}/${prefix}/games 
    for prog in *; do
	if [ -x $prog ]; then
	    echo "making ${D}/${datadir}/applications/$prog.desktop"
	    cat <<STOP > ${D}/${datadir}/applications/$prog.desktop
[Desktop Entry]
Encoding=UTF-8
Name=$prog
Exec=${prefix}/games/$prog
Icon=applications-games
Terminal=false
Type=Application
Categories=Game;
StartupNotify=true
SingleInstance=true
STOP
        fi
    done
}
