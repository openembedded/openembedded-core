require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

SRC_URI += "${KERNELORG_MIRROR}/linux/utils/kernel/kexec/kexec-tools-${PV}.tar.gz \
            file://0002-powerpc-change-the-memory-size-limit.patch \
            file://0001-purgatory-Pass-r-directly-to-linker.patch \
            file://0010-kexec-ARM-Fix-add_buffer_phys_virt-align-issue.patch \
            file://kexec-x32.patch \
            file://0001-Disable-PIE-during-link.patch \
         "

SRC_URI[md5sum] = "78906fdc255656fa2b9996c8acb3ef62"
SRC_URI[sha256sum] = "42dbd0dab9964cd1ed89fa4571c8d13191eb7132b361ade5ac44517c91ecb97e"

SECURITY_PIE_CFLAGS_remove = "-fPIE -pie"

PACKAGES =+ "kexec kdump vmcore-dmesg"

ALLOW_EMPTY_${PN} = "1"
RRECOMMENDS_${PN} = "kexec kdump vmcore-dmesg"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump \
               ${sysconfdir}/sysconfig/kdump.conf \
               ${sysconfdir}/init.d/kdump \
               ${libexecdir}/kdump-helper \
               ${systemd_unitdir}/system/kdump.service \
"

FILES_vmcore-dmesg = "${sbindir}/vmcore-dmesg"

inherit update-rc.d systemd

INITSCRIPT_PACKAGES = "kdump"
INITSCRIPT_NAME_kdump = "kdump"
INITSCRIPT_PARAMS_kdump = "start 56 2 3 4 5 . stop 56 0 1 6 ."

do_install_append () {
        install -d ${D}${sysconfdir}/sysconfig
        install -m 0644 ${WORKDIR}/kdump.conf ${D}${sysconfdir}/sysconfig

        if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
                install -D -m 0755 ${WORKDIR}/kdump ${D}${sysconfdir}/init.d/kdump
        fi

        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
                install -D -m 0755 ${WORKDIR}/kdump ${D}${libexecdir}/kdump-helper
                install -D -m 0644 ${WORKDIR}/kdump.service ${D}${systemd_unitdir}/system/kdump.service
                sed -i -e 's,@LIBEXECDIR@,${libexecdir},g' ${D}${systemd_unitdir}/system/kdump.service
        fi
}
