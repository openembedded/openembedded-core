# grub-efi.bbclass
# Copyright (c) 2011, Intel Corporation.
# All rights reserved.
#
# Released under the MIT license (see packages/COPYING)

# Provide grub-efi specific functions for building bootable images.

# External variables
# ${INITRD} - indicates a filesystem image to use as an initrd (optional)
# ${ROOTFS} - indicates a filesystem image to include as the root filesystem (optional)
# ${LABELS} - a list of targets for the automatic config
# ${APPEND} - an override list of append strings for each label
# ${GRUB_OPTS} - additional options to add to the config, ';' delimited # (optional)
# ${GRUB_TIMEOUT} - timeout before executing the deault label (optional)

do_bootimg[depends] += "grub-efi-${TARGET_ARCH}-native:do_deploy"

GRUBCFG = "${S}/grub.cfg"
GRUB_TIMEOUT ?= "10"
#FIXME: build this from the machine config
GRUB_OPTS ?= "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1"

EFIDIR = "/EFI/BOOT"
GRUB_HDDDIR = "${HDDDIR}${EFIDIR}"
GRUB_ISODIR = "${ISODIR}${EFIDIR}"

grubefi_populate() {
	DEST=$1

	install -d ${DEST}

	install -m 0644 ${STAGING_DIR_HOST}/kernel/bzImage ${DEST}/vmlinuz

	if [ -n "${INITRD}" ] && [ -s "${INITRD}" ]; then 
    		install -m 0644 ${INITRD} ${DEST}/initrd
	fi

	if [ -n "${ROOTFS}" ] && [ -s "${ROOTFS}" ]; then 
    		install -m 0644 ${ROOTFS} ${DEST}/rootfs.img
	fi

	GRUB_IMAGE="bootia32.efi"
	if [ "${TARGET_ARCH}" = "x86_64" ]; then
		GRUB_IMAGE="bootx64.efi"
	fi
	install -m 0644 ${DEPLOY_DIR_IMAGE}/${GRUB_IMAGE} ${DEST}

	install -m 0644 ${GRUBCFG} ${DEST}
}

grubefi_iso_populate() {
	grubefi_populate ${GRUB_ISODIR}
}

grubefi_hddimg_populate() {
	grubefi_populate ${GRUB_HDDDIR}
}

python build_grub_cfg() {
    import sys

    workdir = d.getVar('WORKDIR', True)
    if not workdir:
        bb.error("WORKDIR not defined, unable to package")
        return
       
    labels = d.getVar('LABELS', True)
    if not labels:
        bb.debug(1, "LABELS not defined, nothing to do")
        return
   
    if labels == []:
        bb.debug(1, "No labels, nothing to do")
        return

    cfile = d.getVar('GRUBCFG', True)
    if not cfile:
        raise bb.build.FuncFailed('Unable to read GRUBCFG')

    try:
         cfgfile = file(cfile, 'w')
    except OSError:
        raise bb.build.funcFailed('Unable to open %s' % (cfile))

    cfgfile.write('# Automatically created by OE\n')

    opts = d.getVar('GRUB_OPTS', True)
    if opts:
        for opt in opts.split(';'):
            cfgfile.write('%s\n' % opt)

    cfgfile.write('default=%s\n' % (labels.split()[0]))

    timeout = d.getVar('GRUB_TIMEOUT', True)
    if timeout:
        cfgfile.write('timeout=%s\n' % timeout)
    else:
        cfgfile.write('timeout=50\n')

    for label in labels.split():
        localdata = d.createCopy()

        overrides = localdata.getVar('OVERRIDES', True)
        if not overrides:
            raise bb.build.FuncFailed('OVERRIDES not defined')

        localdata.setVar('OVERRIDES', label + ':' + overrides)
        bb.data.update_data(localdata)

        cfgfile.write('\nmenuentry \'%s\'{\n' % (label))
        cfgfile.write('linux ${EFIDIR}/vmlinuz LABEL=%s' % (label))

        append = localdata.getVar('APPEND', True)
        initrd = localdata.getVar('INITRD', True)

        if append:
            cfgfile.write('%s' % (append))
        cfgfile.write('\n')
   
        if initrd:
            cfgfile.write('initrd ${EFIDIR}/initrd')
        cfgfile.write('\n}\n')

    cfgfile.close()
}
