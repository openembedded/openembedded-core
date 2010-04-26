LICENSE = "LGPL"
DESCRIPTION = "Runtime libraries for parsing and creating MIME mail"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "glib-2.0 zlib"

inherit gnome autotools lib_package binconfig

SRC_URI += "file://iconv-detect.h \
            file://nodolt.patch;patch=1"

EXTRA_OECONF += "--disable-mono"

export ac_cv_have_iconv_detect_h=yes
do_configure_append = "cp ${WORKDIR}/iconv-detect.h ${S}"

# we do not need GNOME 1 gnome-config support
do_install_append () {
	rm -f ${D}${libdir}/gmimeConf.sh
}
