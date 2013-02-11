LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

require diffutils.inc

do_configure_prepend () {
	# Need to remove gettext macros with weird mix of versions
	for i in codeset.m4 gettext_gl.m4 intlmacosx.m4 inttypes-pri.m4 lib-ld_gl.m4 lib-prefix_gl.m4 po_gl.m4 ssize_t.m4 wchar_t.m4 wint_t.m4; do
		rm -f ${S}/m4/$i
	done
}

PR = "${INC_PR}.1"

SRC_URI += "file://remove-gets.patch \
            file://obsolete_automake_macros.patch \
"

SRC_URI[md5sum] = "22e4deef5d8949a727b159d6bc65c1cc"
SRC_URI[sha256sum] = "2aaaebef615be7dc365306a14caa5d273a4fc174f9f10abca8b60e082c054ed3"
