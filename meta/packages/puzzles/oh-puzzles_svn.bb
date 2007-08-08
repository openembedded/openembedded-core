DESCRIPTION = "Portable Puzzle Collection"
LICENSE = "MIT"
SECTION = "x11"
DEPENDS = "gtk+ gconf intltool-native librsvg"
PV = "0.1+svnr${SRCREV}"
PR = "r0"

inherit autotools pkgconfig

SRC_URI = "svn://svn.o-hand.com/repos/;module=oh-puzzles;proto=http"
S = "${WORKDIR}/${PN}"

do_install_append () {
    mv ${D}${bindir} ${D}/usr/games

    install -d ${D}/${datadir}/applications/

    cd ${D}/${prefix}/games
    for prog in *; do
	if [ -x $prog ]; then
            # Convert prog to Title Case
            title=$(echo $prog | sed 's/\(^\| \)./\U&/g')
	    echo "making ${D}/${datadir}/applications/$prog.desktop"
	    cat <<STOP > ${D}/${datadir}/applications/$prog.desktop
[Desktop Entry]
Encoding=UTF-8
Name=$title
Exec=${prefix}/games/$prog
Icon=applications-games
Terminal=false
Type=Application
Categories=Game
StartupNotify=true
SingleInstance=true
Comment=Play $title.
STOP
        fi
    done
}

FILES_${PN} += "/usr/games/*"
FILES_${PN}-dbg += "/usr/games/.debug/*"
