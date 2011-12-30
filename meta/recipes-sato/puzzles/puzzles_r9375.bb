DESCRIPTION="Simon Tatham's Portable Puzzle Collection"
HOMEPAGE="http://www.chiark.greenend.org.uk/~sgtatham/puzzles/"

DEPENDS = "gtk+ libxt"
PR = "r0"
MOD_PV = "${@d.getVar('PV',1)[1:]}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENCE;md5=9928b60f3b78be315b7ab699c1b03ff5"

# Upstream updates puzzles.tar.gz for the new release, so checksums seem to be changing regularly right now
#SRC_URI = "svn://ixion.tartarus.org/main;module=puzzles;rev=${MOD_PV}"
SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz"
SRC_URI[md5sum] = "35e85cefb59d39cd22bf232673857727"
SRC_URI[sha256sum] = "ae94dc8a654582c0cab61ce4699e25027ed720c06b411e5202144a5114fd338b"


S = "${WORKDIR}/${BPN}-${PV}"

do_configure () {
	./mkfiles.pl
}

do_compile_prepend = " \
        export XLDFLAGS='${LDFLAGS} `${STAGING_BINDIR_NATIVE}/pkg-config gtk+-2.0 --libs`'; \
        export XLFLAGS=-lm \
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
            # Convert prog to Title Case
            title=$(echo $prog | sed 's/\(^\| \)./\U&/g')
	    echo "making ${D}/${datadir}/applications/$prog.desktop"
	    cat <<STOP > ${D}/${datadir}/applications/$prog.desktop
[Desktop Entry]
Name=$title
Exec=${prefix}/games/$prog
Icon=applications-games
Terminal=false
Type=Application
Categories=Game;
StartupNotify=true
X-MB-SingleInstance=true
STOP
        fi
    done
}
