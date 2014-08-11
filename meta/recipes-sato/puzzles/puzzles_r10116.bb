SUMMARY = "Simon Tatham's Portable Puzzle Collection"
HOMEPAGE = "http://www.chiark.greenend.org.uk/~sgtatham/puzzles/"

DEPENDS = "gtk+ libxt"
MOD_PV = "${@d.getVar('PV',1)[1:]}"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENCE;md5=33bcd4bce8f3c197f2aefbdbd2d299bc"

SRC_URI = "svn://svn.tartarus.org/sgt;module=puzzles;rev=${MOD_PV} \
           file://fix-compiling-failure-with-option-g-O.patch \
"

S = "${WORKDIR}/${BPN}"

inherit autotools-brokensep

do_configure_prepend () {
    ./mkfiles.pl
}

FILES_${PN} = "${prefix}/bin/* ${datadir}/applications/*"
FILES_${PN}-dbg += "${prefix}/bin/.debug"

do_install () {
    rm -rf ${D}/*
    export prefix=${D}
    export DESTDIR=${D}
    install -d ${D}/${prefix}/bin/
    oe_runmake install


    install -d ${D}/${datadir}/applications/

    # Create desktop shortcuts
    cd ${D}/${prefix}/bin
    for prog in *; do
	if [ -x $prog ]; then
            # Convert prog to Title Case
            title=$(echo $prog | sed 's/\(^\| \)./\U&/g')
	    echo "making ${D}/${datadir}/applications/$prog.desktop"
	    cat <<STOP > ${D}/${datadir}/applications/$prog.desktop
[Desktop Entry]
Name=$title
Exec=${prefix}/bin/$prog
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
