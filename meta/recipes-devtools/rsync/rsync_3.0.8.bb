require rsync.inc

PR = "r0"

SRC_URI += "file://acinclude.m4"

SRC_URI[md5sum] = "0ee8346ce16bdfe4c88a236e94c752b4"
SRC_URI[sha256sum] = "4b3fc271f4c96036b4c73fb019be078e4d8cce2defe1e7ae7cde1117859a2114"

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
