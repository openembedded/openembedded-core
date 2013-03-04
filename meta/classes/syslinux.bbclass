# syslinux.bbclass
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

# Provide syslinux specific functions for building bootable images.

# External variables
# ${INITRD} - indicates a filesystem image to use as an initrd (optional)
# ${ROOTFS} - indicates a filesystem image to include as the root filesystem (optional)
# ${AUTO_SYSLINUXMENU} - set this to 1 to enable creating an automatic menu
# ${LABELS} - a list of targets for the automatic config
# ${APPEND} - an override list of append strings for each label
# ${SYSLINUX_OPTS} - additional options to add to the syslinux file ';' delimited
# ${SYSLINUX_SPLASH} - A background for the vga boot menu if using the boot menu
# ${SYSLINUX_SERIAL} - Set an alternate serial port or turn off serial with empty string

do_bootimg[depends] += "syslinux:do_populate_sysroot \
                        syslinux-native:do_populate_sysroot"

SYSLINUXCFG  = "${S}/syslinux.cfg"

ISOLINUXDIR = "/isolinux"
SYSLINUXDIR = "/"
SYSLINUX_SERIAL ?= "0 115200"
ISO_BOOTIMG = "isolinux/isolinux.bin"
ISO_BOOTCAT = "isolinux/boot.cat"
MKISOFS_OPTIONS = "-no-emul-boot -boot-load-size 4 -boot-info-table"
APPEND_prepend = " ${SYSLINUX_ROOT} "

syslinux_populate() {
	DEST=$1
	BOOTDIR=$2
	CFGNAME=$3

	install -d ${DEST}${BOOTDIR}

	# Install the config files
	install -m 0644 ${SYSLINUXCFG} ${DEST}${BOOTDIR}/${CFGNAME}
}

syslinux_iso_populate() {
	syslinux_populate ${ISODIR} ${ISOLINUXDIR} isolinux.cfg
	install -m 0644 ${STAGING_LIBDIR}/syslinux/isolinux.bin ${ISODIR}${ISOLINUXDIR}
	if [ "${AUTO_SYSLINUXMENU}" = 1 ] ; then
		install -m 0644 ${STAGING_LIBDIR}/syslinux/vesamenu.c32 ${ISODIR}${ISOLINUXDIR}/vesamenu.c32
		if [ "${SYSLINUX_SPLASH}" != "" ] ; then
			install -m 0644 ${SYSLINUX_SPLASH} ${ISODIR}${ISOLINUXDIR}/splash.lss
		fi
	fi
}

syslinux_hddimg_populate() {
	syslinux_populate ${HDDDIR} ${SYSLINUXDIR} syslinux.cfg
	install -m 0444 ${STAGING_LIBDIR}/syslinux/ldlinux.sys ${HDDDIR}${SYSLINUXDIR}/ldlinux.sys
	if [ "${AUTO_SYSLINUXMENU}" = 1 ] ; then
		install -m 0644 ${STAGING_LIBDIR}/syslinux/vesamenu.c32 ${HDDDIR}${SYSLINUXDIR}/vesamenu.c32
		if [ "${SYSLINUX_SPLASH}" != "" ] ; then
			install -m 0644 ${SYSLINUX_SPLASH} ${HDDDIR}${SYSLINUXDIR}/splash.lss
		fi
	fi
}

syslinux_hddimg_install() {
	syslinux ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hddimg
}

python build_syslinux_cfg () {
    import copy
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

    cfile = d.getVar('SYSLINUXCFG', True)
    if not cfile:
        raise bb.build.FuncFailed('Unable to read SYSLINUXCFG')

    try:
        cfgfile = file(cfile, 'w')
    except OSError:
        raise bb.build.funcFailed('Unable to open %s' % (cfile))

    cfgfile.write('# Automatically created by OE\n')

    opts = d.getVar('SYSLINUX_OPTS', True)

    if opts:
        for opt in opts.split(';'):
            cfgfile.write('%s\n' % opt)

    cfgfile.write('ALLOWOPTIONS 1\n');
    syslinux_serial = d.getVar('SYSLINUX_SERIAL', True)
    if syslinux_serial:
        cfgfile.write('SERIAL %s\n' % syslinux_serial)

    menu = d.getVar('AUTO_SYSLINUXMENU', True)

    if menu and syslinux_serial:
        cfgfile.write('DEFAULT Graphics console %s\n' % (labels.split()[0]))
    else:
        cfgfile.write('DEFAULT %s\n' % (labels.split()[0]))

    timeout = d.getVar('SYSLINUX_TIMEOUT', True)

    if timeout:
        cfgfile.write('TIMEOUT %s\n' % timeout)
    else:
        cfgfile.write('TIMEOUT 50\n')

    prompt = d.getVar('SYSLINUX_PROMPT', True)
    if prompt:
        cfgfile.write('PROMPT %s\n' % prompt)
    else:
        cfgfile.write('PROMPT 1\n')

    if menu:
        cfgfile.write('ui vesamenu.c32\n')
        cfgfile.write('menu title Select kernel options and boot kernel\n')
        cfgfile.write('menu tabmsg Press [Tab] to edit, [Return] to select\n')
        splash = d.getVar('SYSLINUX_SPLASH', True)
        if splash:
            cfgfile.write('menu background splash.lss\n')
    
    for label in labels.split():
        localdata = bb.data.createCopy(d)

        overrides = localdata.getVar('OVERRIDES', True)
        if not overrides:
            raise bb.build.FuncFailed('OVERRIDES not defined')

        localdata.setVar('OVERRIDES', label + ':' + overrides)
        bb.data.update_data(localdata)
    
        btypes = [ [ "", "console=tty0" ] ]
        if menu and syslinux_serial:
            btypes = [ [ "Graphics console ", " console=tty0" ],
                [ "Serial console ",  " console=ttyS0,115200" ] ]

        for btype in btypes:
            cfgfile.write('LABEL %s%s\nKERNEL /vmlinuz\n' % (btype[0], label))

            append = localdata.getVar('APPEND', True)
            initrd = localdata.getVar('INITRD', True)

            if append:
                cfgfile.write('APPEND ')

                if initrd:
                    cfgfile.write('initrd=/initrd ')

                cfgfile.write('LABEL=%s '% (label))

                cfgfile.write('%s %s\n' % (append, btype[1]))
            else:
                cfgfile.write('APPEND %s\n' % btype[1])

    cfgfile.close()
}
