require rsync.inc

PR = "r1"

SRC_URI += "file://acinclude.m4"

SRC_URI[md5sum] = "b53525900817cf1ba7ad3a516ab5bfe9"
SRC_URI[sha256sum] = "9ee00d16c023c486328cbb61f59928a954b24b7b7173c3517ebb0d6c3edfe7c8"

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
