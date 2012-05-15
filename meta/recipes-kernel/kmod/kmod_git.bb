# Copyright (C) 2012 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

require kmod.inc

PR = "${INC_PR}.2"

PROVIDES += "module-init-tools-insmod-static module-init-tools-depmod module-init-tools"
RPROVIDES_${PN} += "module-init-tools-insmod-static module-init-tools-depmod module-init-tools"
RCONFLICTS_${PN} += "module-init-tools-insmod-static module-init-tools-depmod module-init-tools"
RREPLACES_${PN} += "module-init-tools-insmod-static module-init-tools-depmod module-init-tools"

# to force user to remove old module-init-tools and replace them with kmod variants
RCONFLICTS_libkmod2 += "module-init-tools-insmod-static module-init-tools-depmod module-init-tools"

# autotools set prefix to /usr, however we want them in /bin and /sbin
bindir = "${base_bindir}"
sbindir = "${base_sbindir}"
# libdir = "${base_libdir}"

do_install_append () {
        install -dm755 ${D}${base_bindir}
        install -dm755 ${D}${base_sbindir}
        # add symlinks to kmod
        ln -s ..${base_bindir}/kmod ${D}${base_bindir}/lsmod.kmod
        for tool in {ins,rm,dep}mod mod{info,probe}; do
                ln -s ..${base_bindir}/kmod ${D}${base_sbindir}/${tool}.kmod
        done
        # configuration directories
        install -dm755 ${D}${base_libdir}/depmod.d
        install -dm755 ${D}${base_libdir}/modprobe.d
        install -dm755 ${D}${sysconfdir}/depmod.d
        install -dm755 ${D}${sysconfdir}/modprobe.d

        # install depmod.d file for search/ dir
        install -Dm644 "${WORKDIR}/depmod-search.conf" "${D}${base_libdir}/depmod.d/search.conf"
}

pkg_postinst_kmod() {
        for f in sbin/insmod sbin/modprobe sbin/rmmod sbin/modinfo; do
                bn=`basename $f`
                update-alternatives --install /$f $bn /$f.kmod 60
        done
        update-alternatives --install /bin/lsmod bin-lsmod /bin/lsmod.kmod 60
        update-alternatives --install /sbin/lsmod lsmod /bin/lsmod.kmod 60
        update-alternatives --install /sbin/depmod depmod /sbin/depmod.kmod 60
}

pkg_prerm_kmod() {
        for f in sbin/insmod sbin/modprobe sbin/rmmod sbin/modinfo; do
                bn=`basename $f`
                update-alternatives --remove $bn /$f.kmod
        done
        update-alternatives --remove bin-lsmod /bin/lsmod.kmod
        update-alternatives --remove lsmod /bin/lsmod.kmod
        update-alternatives --remove depmod /sbin/depmod.kmod
}

PACKAGES =+ "libkmod"

FILES_libkmod = "${base_libdir}/libkmod*${SOLIBS} ${libdir}/libkmod*${SOLIBS}"
FILES_${PN} += "${base_libdir}/depmod.d ${base_libdir}/modprobe.d"
