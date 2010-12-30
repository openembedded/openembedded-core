require matchbox-theme-sato.inc

DEPENDS = "matchbox-wm-2"
PV = "0.1+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=matchbox-sato;proto=http \
          file://png_rename.patch"

S = "${WORKDIR}/matchbox-sato"

EXTRA_OECONF = "--disable-matchbox-1 --enable-matchbox-2"
