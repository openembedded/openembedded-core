DESCRIPTION="Simon Tatham's Portable Puzzle Collection"
HOMEPAGE="http://www.chiark.greenend.org.uk/~sgtatham/puzzles/"

DEPENDS = "gtk+ libxt"
PR = "r0"
MOD_PV = "${@bb.data.getVar('PV',d,1)[1:]}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENCE;md5=9928b60f3b78be315b7ab699c1b03ff5"

SRC_URI = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/puzzles-${PV}.tar.gz"

SRC_URI[md5sum] = "639ddf8d1e97d7d4caa35d0078e3293d"
SRC_URI[sha256sum] = "c74f7c3e5316efe1cfe6de5e8641ccd9827ca4dae56ec68ad59924b7d6c1bac7"
#SRC_URI = "svn://ixion.tartarus.org/main;module=puzzles;rev=${MOD_PV}"

S = "${WORKDIR}/${PN}-${PV}"

do_configure () {
	./mkfiles.pl
}

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
