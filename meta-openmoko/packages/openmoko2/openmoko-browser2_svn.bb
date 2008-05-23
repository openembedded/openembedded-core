DESCRIPTION = "The Openmoko Webbrowser"
SECTION = "openmoko/apps"
DEPENDS += "intltool libmokoui2 check webkit-gtk"
PV = "0.0.1+svnr${SRCREV}"
PR = "r2"

inherit openmoko2
LDFLAGS_append = " -Wl,-rpath-link,${CROSS_DIR}/${TARGET_SYS}/lib"

SRC_URI += "file://webkit-update.patch;patch=1;minrev=3646;maxrev=4171"

#SRC_URI += "file://fingerscroll.diff;patch=1"

do_compile_prepend() {
        find ${S} -name Makefile | xargs sed -i s:'-I/usr/include':"-I${STAGING_INCDIR}":g
}

