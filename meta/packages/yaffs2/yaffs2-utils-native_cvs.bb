require yaffs2-utils.inc

inherit native

DEPENDS = "mtd-utils-native"
PR = "r1"

CFLAGS += "-I.. -DCONFIG_YAFFS_UTIL"

do_stage() {
    for i in mkyaffsimage mkyaffs2image; do
        install -m 0755 utils/$i ${STAGING_BINDIR_NATIVE}
    done
}
