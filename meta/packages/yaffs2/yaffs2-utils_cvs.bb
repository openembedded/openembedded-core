require yaffs2-utils.inc

DEPENDS = "mtd-utils"
PR = "r1"

do_install() {
	install -d ${D}${sbindir}
    for i in mkyaffsimage mkyaffs2image; do
        install -m 0755 utils/$i ${D}${sbindir}
    done
}
