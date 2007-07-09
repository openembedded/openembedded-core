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

# can't do adduser stuff offline
if [ "x$D" != "x" ]; then
  exit 1
fi

# set up some links to firmware and modules in initrd
	mkdir -p /lib/firmware
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/3825.arm /lib/firmware/3825.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/3826.arm /lib/firmware/3826.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/bc4fw.bin /lib/firmware/bc4fw.bin
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/brf6150fw.bin /lib/firmware/brf6150fw.bin
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/mtlm3825.arm /lib/firmware/mtlm3825.arm
        ln -sf /mnt/initfs/usr/lib/hotplug/firmware/mtlm3826.arm /lib/firmware/mtlm3826.arm

#	rm -rf /lib/modules
#	ln -s /mnt/initfs/lib/modules /lib/modules
}
