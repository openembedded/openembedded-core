LICENSE    = "GPL"
PR         = "r6"

DEPENDS    = "base-passwd"

SRC_URI    = "file://fixup-770.sh"

COMPATIBLE_MACHINE = "(nokia770|nokia800)"

inherit update-rc.d

INITSCRIPT_NAME = "fixup-770.sh"
INITSCRIPT_PARAMS = "defaults 01"

FILES_${PN} = "${sysconfdir} ${libdir}"

do_install () {
        install -d ${D}${sysconfdir}/init.d
        install -m 755 ${WORKDIR}/fixup-770.sh ${D}/${sysconfdir}/init.d/fixup-770.sh

	install -d ${D}/lib/firmware
}

pkg_postinst () {
#!/bin/sh

# set up some links to firmware and modules in initrd
	mkdir -p $D/lib/firmware
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/3825.arm $D/lib/firmware/3825.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/3826.arm $D/lib/firmware/3826.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/bc4fw.bin $D/lib/firmware/bc4fw.bin
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/brf6150fw.bin $D/lib/firmware/brf6150fw.bin
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/mtlm3825.arm $D/lib/firmware/mtlm3825.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/mtlm3826.arm $D/lib/firmware/mtlm3826.arm

#	rm -rf $D/lib/modules
#	ln -s /mnt/initfs/lib/modules $D/lib/modules
}
