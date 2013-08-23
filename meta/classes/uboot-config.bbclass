# Allow easy override of U-Boot config for a machine
#
# The format to specify it, in the machine, is:
#
# UBOOT_CONFIG ??= <default>
# UBOOT_CONFIG[foo] = "config,images"
#
# Copyright 2013 (C) O.S. Systems Software LTDA.

addhandler uboot_config_eventhandler
uboot_config_eventhandler[eventmask] = "bb.event.ConfigParsed"
python uboot_config_eventhandler() {
    ubootconfigflags = e.data.getVarFlags('UBOOT_CONFIG')
    if not ubootconfigflags:
        return

    ubootconfig = (e.data.getVar('UBOOT_CONFIG', True) or "").split()
    if len(ubootconfig) > 1:
        raise bb.parse.SkipPackage('You can only have a single default for UBOOT_CONFIG.')
    elif len(ubootconfig) == 0:
        raise bb.parse.SkipPackage('You must set a default in UBOOT_CONFIG.')
    ubootconfig = ubootconfig[0]

    for f, v in ubootconfigflags.items():
        if f == 'defaultval':
            continue

        items = v.split(',')
        if items[0] and len(items) > 2:
            raise bb.parse.SkipPackage('Only config,images can be specified!')

        if ubootconfig == f:
            bb.debug(1, "Setting UBOOT_MACHINE to %s." % items[0])
            e.data.setVar('UBOOT_MACHINE', items[0])

            if items[1]:
                bb.debug(1, "Appending '%s' to IMAGE_FSTYPES." % items[1])
                e.data.appendVar('IMAGE_FSTYPES', ' ' + items[1])
}
