require yaffs2-utils.inc

inherit native

DEPENDS = "mtd-utils-native"

do_stage() {
    for i in mkyaffsimage mkyaffs2image; do
        install -m 0755 utils/$i ${STAGING_BINDIR_NATIVE}
    done
}
