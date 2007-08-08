require shasum.inc

inherit native

INHIBIT_DEFAULT_DEPS = "1"
PATCHTOOL = "patch"

do_fetch[depends] = ""
do_populate_staging() {
    install ${S}/oe_sha256sum ${STAGING_BINDIR}
}
