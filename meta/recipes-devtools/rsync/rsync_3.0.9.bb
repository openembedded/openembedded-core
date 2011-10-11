require rsync.inc

PR = "r0"

SRC_URI += "file://acinclude.m4"

SRC_URI[md5sum] = "5ee72266fe2c1822333c407e1761b92b"
SRC_URI[sha256sum] = "30f10f8dd5490d28240d4271bb652b1da7a60b22ed2b9ae28090668de9247c05"

EXTRA_OECONF += "--disable-xattr-support --disable-acl-support"

# rsync 3.0 uses configure.sh instead of configure, and
# makefile checks the existence of configure.sh
do_configure_prepend () {
	rm -f configure configure.sh
	cp -f ${WORKDIR}/acinclude.m4 ${S}/
}

do_configure_append () {
	cp -f configure configure.sh
}
