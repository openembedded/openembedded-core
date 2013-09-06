DESCRIPTION = "Systemd a init replacement"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/systemd"

LICENSE = "GPLv2 & LGPLv2.1 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe \
                    file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE.MIT;md5=544799d0b492f119fa04641d1b8868ed"

PROVIDES = "udev"

PE = "1"

DEPENDS = "kmod docbook-sgml-dtd-4.1-native intltool-native gperf-native acl readline dbus libcap libcgroup glib-2.0 qemu-native util-linux"
DEPENDS += "${@base_contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

SECTION = "base/shell"

inherit gtk-doc useradd pkgconfig autotools perlnative update-rc.d update-alternatives qemu systemd

SRC_URI = "http://www.freedesktop.org/software/systemd/systemd-${PV}.tar.xz \
           file://0001-use-CAP_MKNOD-ConditionCapability.patch \
           file://0001-Use-bin-mkdir-instead-of-host-mkdir-path.patch \
           file://binfmt-install.patch \
           file://touchscreen.rules \
           ${UCLIBCPATCHES} \
           file://00-create-volatile.conf \
           file://init \
          "
SRC_URI[md5sum] = "89e36f2d3ba963020b72738549954cbc"
SRC_URI[sha256sum] = "4c993de071118ea1df7ffc4be26ef0b0d78354ef15b2743a2783d20edfcde9de"

UCLIBCPATCHES = ""
UCLIBCPATCHES_libc-uclibc = "file://systemd-pam-configure-check-uclibc.patch \
                             file://systemd-pam-fix-execvpe.patch \
                             file://systemd-pam-fix-fallocate.patch \
                             file://systemd-pam-fix-getty-unit.patch \
                             file://systemd-pam-fix-mkostemp.patch \
                             file://systemd-pam-fix-msformat.patch \
                             file://optional_secure_getenv.patch \
                             file://0001-uClibc-doesn-t-implement-pwritev-preadv.patch \
                            "
LDFLAGS_libc-uclibc_append = " -lrt"

GTKDOC_DOCDIR = "${S}/docs/"

PACKAGECONFIG ??= "xz tcp-wrappers"
# Sign the journal for anti-tampering
PACKAGECONFIG[gcrypt] = "--enable-gcrypt,--disable-gcrypt,libgcrypt"
# regardless of PACKAGECONFIG, libgcrypt is always required to expand
# the AM_PATH_LIBGCRYPT autoconf macro
DEPENDS += "libgcrypt"
# Compress the journal
PACKAGECONFIG[xz] = "--enable-xz,--disable-xz,xz"
PACKAGECONFIG[tcp-wrappers] = "--enable-tcpwrap,--disable-tcpwrap,tcp-wrappers"

CACHED_CONFIGUREVARS = "ac_cv_path_KILL=${base_bindir}/kill"

# Helper variables to clarify locations.  This mirrors the logic in systemd's
# build system.
rootprefix ?= "${base_prefix}"
rootlibdir ?= "${base_libdir}"
rootlibexecdir = "${rootprefix}/lib"

# The gtk+ tools should get built as a separate recipe e.g. systemd-tools
EXTRA_OECONF = " --with-rootprefix=${rootprefix} \
                 --with-rootlibdir=${rootlibdir} \
                 ${@base_contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)} \
                 --enable-xz \
                 --disable-manpages \
                 --disable-coredump \
                 --disable-introspection \
                 --disable-tcpwrap \
                 --enable-split-usr \
                 --disable-microhttpd \
                 --without-python \
                 --with-sysvrcnd-path=${sysconfdir} \
                 --with-firmware-path=/lib/firmware \
                 ac_cv_path_KILL=${base_bindir}/kill \
               "
# uclibc does not have NSS
EXTRA_OECONF_append_libc-uclibc = " --disable-myhostname "

do_configure_prepend() {
	export CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS} ${HOST_CC_ARCH}"
	export KMOD="${base_bindir}/kmod"
	sed -i -e 's:=/root:=${ROOT_HOME}:g' ${S}/units/*.service*
}

do_install() {
	autotools_do_install
	install -d ${D}/${base_sbindir}
	# Provided by a separate recipe
	rm ${D}${systemd_unitdir}/system/serial-getty* -f

	# Provide support for initramfs
	ln -s ${rootlibexecdir}/systemd/systemd ${D}/init
	ln -s ${rootlibexecdir}/systemd/systemd-udevd ${D}/${base_sbindir}/udevd

	# Create dir for journal
	install -d ${D}${localstatedir}/log/journal

	# Create machine-id
	# 20:12 < mezcalero> koen: you have three options: a) run systemd-machine-id-setup at install time, b) have / read-only and an empty file there (for stateless) and c) boot with / writable
	touch ${D}${sysconfdir}/machine-id

	install -m 0644 ${WORKDIR}/*.rules ${D}${sysconfdir}/udev/rules.d/

	install -m 0644 ${WORKDIR}/00-create-volatile.conf ${D}${sysconfdir}/tmpfiles.d/

	if ${@base_contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
		install -d ${D}${sysconfdir}/init.d
		install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/systemd-udevd
		sed -i s%@UDEVD@%${rootlibexecdir}/systemd/systemd-udevd% ${D}${sysconfdir}/init.d/systemd-udevd
	fi
}

python populate_packages_prepend (){
    systemdlibdir = d.getVar("rootlibdir", True)
    do_split_packages(d, systemdlibdir, '^lib(.*)\.so\.*', 'lib%s', 'Systemd %s library', extra_depends='', allow_links=True)
}
PACKAGES_DYNAMIC += "^lib(udev|gudev|systemd).*"

PACKAGES =+ "${PN}-gui ${PN}-vconsole-setup ${PN}-initramfs ${PN}-analyze ${PN}-kernel-install ${PN}-rpm-macros ${PN}-binfmt"

SYSTEMD_PACKAGES = "${PN}-binfmt"
SYSTEMD_SERVICE_${PN}-binfmt = "systemd-binfmt.service"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "-r lock; -r systemd-journal"

FILES_${PN}-analyze = "${bindir}/systemd-analyze"

FILES_${PN}-initramfs = "/init"
RDEPENDS_${PN}-initramfs = "${PN}"

FILES_${PN}-gui = "${bindir}/systemadm"

FILES_${PN}-vconsole-setup = "${rootlibexecdir}/systemd/systemd-vconsole-setup \
                              ${systemd_unitdir}/system/systemd-vconsole-setup.service \
                              ${systemd_unitdir}/system/sysinit.target.wants/systemd-vconsole-setup.service"

FILES_${PN}-kernel-install = "${bindir}/kernel-install \
                              ${sysconfdir}/kernel/ \
                              ${exec_prefix}/lib/kernel \
                             "
FILES_${PN}-rpm-macros = "${libdir}/rpm \
                         "

FILES_${PN}-binfmt = "${sysconfdir}/binfmt.d/ \
                      ${exec_prefix}/lib/binfmt.d \
                      ${rootlibexecdir}/systemd/systemd-binfmt \
                      ${systemd_unitdir}/system/proc-sys-fs-binfmt_misc.* \
                      ${systemd_unitdir}/system/systemd-binfmt.service"
RRECOMMENDS_${PN}-binfmt = "kernel-module-binfmt-misc"

RRECOMMENDS_${PN}-vconsole-setup = "kbd kbd-consolefonts"

CONFFILES_${PN} = "${sysconfdir}/systemd/journald.conf \
                ${sysconfdir}/systemd/logind.conf \
                ${sysconfdir}/systemd/system.conf \
                ${sysconfdir}/systemd/user.conf"

FILES_${PN} = " ${base_bindir}/* \
                ${datadir}/bash-completion \
                ${datadir}/dbus-1/services \
                ${datadir}/dbus-1/system-services \
                ${datadir}/polkit-1 \
                ${datadir}/${BPN} \
                ${sysconfdir}/bash_completion.d/ \
                ${sysconfdir}/dbus-1/ \
                ${sysconfdir}/machine-id \
                ${sysconfdir}/modules-load.d/ \
                ${sysconfdir}/sysctl.d/ \
                ${sysconfdir}/systemd/ \
                ${sysconfdir}/tmpfiles.d/ \
                ${sysconfdir}/xdg/ \
                ${sysconfdir}/init.d/README \
                ${rootlibexecdir}/systemd/* \
                ${systemd_unitdir}/* \
                ${base_libdir}/security/*.so \
                ${libdir}/libnss_myhostname.so.2 \
                /cgroup \
                ${bindir}/systemd* \
                ${bindir}/localectl \
                ${bindir}/hostnamectl \
                ${bindir}/timedatectl \
                ${bindir}/bootctl \
                ${bindir}/kernel-install \
                ${exec_prefix}/lib/tmpfiles.d/*.conf \
                ${exec_prefix}/lib/systemd \
                ${exec_prefix}/lib/modules-load.d \
                ${exec_prefix}/lib/sysctl.d \
                ${localstatedir} \
                /lib/udev/rules.d/70-uaccess.rules \
                /lib/udev/rules.d/71-seat.rules \
                /lib/udev/rules.d/73-seat-late.rules \
                /lib/udev/rules.d/99-systemd.rules \
               "

FILES_${PN}-dbg += "${rootlibdir}/.debug ${systemd_unitdir}/.debug ${systemd_unitdir}/*/.debug ${base_libdir}/security/.debug/"
FILES_${PN}-dev += "${base_libdir}/security/*.la ${datadir}/dbus-1/interfaces/ ${sysconfdir}/rpm/macros.systemd"

RDEPENDS_${PN} += "kmod dbus util-linux-mount"

RRECOMMENDS_${PN} += "systemd-serialgetty systemd-compat-units \
                      util-linux-agetty \
                      util-linux-fsck e2fsprogs-e2fsck \
                      kernel-module-autofs4 kernel-module-unix kernel-module-ipv6 \
"

PACKAGES =+ "udev-dbg udev udev-utils udev-hwdb"

FILES_udev-dbg += "/lib/udev/.debug"

RDEPENDS_udev += "udev-utils"
RPROVIDES_udev = "hotplug"
RRECOMMENDS_udev += "udev-hwdb"

RDEPENDS_udev-hwdb += "udev-utils"

FILES_udev += "${base_sbindir}/udevd \
               ${rootlibexecdir}/systemd/systemd-udevd \
               ${rootlibexecdir}/udev/accelerometer \
               ${rootlibexecdir}/udev/ata_id \
               ${rootlibexecdir}/udev/cdrom_id \
               ${rootlibexecdir}/udev/collect \
               ${rootlibexecdir}/udev/findkeyboards \
               ${rootlibexecdir}/udev/keyboard-force-release.sh \
               ${rootlibexecdir}/udev/keymap \
               ${rootlibexecdir}/udev/mtd_probe \
               ${rootlibexecdir}/udev/scsi_id \
               ${rootlibexecdir}/udev/v4l_id \
               ${rootlibexecdir}/udev/keymaps \
               ${rootlibexecdir}/udev/rules.d/4*.rules \
               ${rootlibexecdir}/udev/rules.d/5*.rules \
               ${rootlibexecdir}/udev/rules.d/6*.rules \
               ${rootlibexecdir}/udev/rules.d/70-power-switch.rules \
               ${rootlibexecdir}/udev/rules.d/75*.rules \
               ${rootlibexecdir}/udev/rules.d/78*.rules \
               ${rootlibexecdir}/udev/rules.d/8*.rules \
               ${rootlibexecdir}/udev/rules.d/95*.rules \
               ${sysconfdir}/udev \
               ${sysconfdir}/init.d/systemd-udevd \
               ${systemd_unitdir}/system/*udev* \
               ${systemd_unitdir}/system/*.wants/*udev* \
              "

FILES_udev-utils = "${base_bindir}/udevadm ${datadir}/bash-completion/completions/udevadm"

FILES_udev-hwdb = "${rootlibexecdir}/udev/hwdb.d"

INITSCRIPT_PACKAGES = "udev"
INITSCRIPT_NAME_udev = "systemd-udevd"
INITSCRIPT_PARAMS_udev = "start 03 S ."

python __anonymous() {
    features = d.getVar("DISTRO_FEATURES", True).split()
    if "sysvinit" not in features:
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

# TODO:
# u-a for runlevel and telinit

ALTERNATIVE_${PN} = "init halt reboot shutdown poweroff"

ALTERNATIVE_TARGET[init] = "${rootlibexecdir}/systemd/systemd"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_PRIORITY[init] ?= "300"

ALTERNATIVE_TARGET[halt] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[halt] = "${base_sbindir}/halt"
ALTERNATIVE_PRIORITY[halt] ?= "300"

ALTERNATIVE_TARGET[reboot] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[reboot] = "${base_sbindir}/reboot"
ALTERNATIVE_PRIORITY[reboot] ?= "300"

ALTERNATIVE_TARGET[shutdown] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[shutdown] = "${base_sbindir}/shutdown"
ALTERNATIVE_PRIORITY[shutdown] ?= "300"

ALTERNATIVE_TARGET[poweroff] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[poweroff] = "${base_sbindir}/poweroff"
ALTERNATIVE_PRIORITY[poweroff] ?= "300"

pkg_postinst_udev-hwdb () {
	if test -n "$D"; then
		${@qemu_run_binary(d, '$D', '${base_bindir}/udevadm')} hwdb --update \
			--root $D
	else
		udevadm hwdb --update
	fi
}

pkg_prerm_udev-hwdb () {
	if test -n "$D"; then
		exit 1
	fi

	rm -f ${sysconfdir}/udev/hwdb.bin
}

# As this recipe builds udev, respect the systemd DISTRO_FEATURE so we don't try
# building udev and systemd in world builds.
python () {
    if not oe.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' not in DISTRO_FEATURES")
}
