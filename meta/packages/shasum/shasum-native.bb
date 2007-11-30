require shasum.inc

inherit native

INHIBIT_DEFAULT_DEPS = "1"
PATCHTOOL = "patch"

do_fetch[depends] = ""
do_stage() {
    install -d ${STAGING_BINDIR}
    install ${S}/oe_sha256sum ${STAGING_BINDIR}
}
