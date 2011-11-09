# Support for device tree generation
FILES_kernel-devicetree = "/boot/devicetree*"
KERNEL_DEVICETREE_FLAGS = "-R 8 -p 0x3000"

python __anonymous () {
    import bb

    devicetree = d.getVar("KERNEL_DEVICETREE", 1) or ''
    if devicetree:
	depends = d.getVar("DEPENDS", 1)
	d.setVar("DEPENDS", "%s dtc-native" % depends)
	packages = d.getVar("PACKAGES", 1)
	d.setVar("PACKAGES", "%s kernel-devicetree" % packages)
}

do_install_append() {
    if test -n "${KERNEL_DEVICETREE}"; then
	dtc -I dts -O dtb ${KERNEL_DEVICETREE_FLAGS} -o devicetree ${KERNEL_DEVICETREE}
	install -m 0644 devicetree ${D}/boot/devicetree-${KERNEL_VERSION}
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 devicetree ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE_BASE_NAME}.dtb
	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
	ln -sf ${KERNEL_IMAGE_BASE_NAME}.dtb ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
    fi
}

