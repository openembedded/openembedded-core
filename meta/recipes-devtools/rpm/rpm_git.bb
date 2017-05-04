SUMMARY = "The RPM package management system"
DESCRIPTION = "The RPM Package Manager (RPM) is a powerful command line driven \
package management system capable of installing, uninstalling, \
verifying, querying, and updating software packages. Each software \
package consists of an archive of files along with information about \
the package like its version, a description, etc."

SUMMARY_${PN}-dev = "Development files for manipulating RPM packages"
DESCRIPTION_${PN}-dev = "This package contains the RPM C library and header files. These \
development files will simplify the process of writing programs that \
manipulate RPM packages and databases. These files are intended to \
simplify the process of creating graphical package managers or any \
other tools that need an intimate knowledge of RPM packages in order \
to function."

SUMMARY_python3-rpm = "Python bindings for apps which will manupulate RPM packages"
DESCRIPTION_python3-rpm = "The python3-rpm package contains a module that permits applications \
written in the Python programming language to use the interface \
supplied by the RPM Package Manager libraries."

HOMEPAGE = "http://www.rpm.org"

# libraries are also LGPL - how to express this?
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=f5259151d26ff18e78023450a5ac8d96"

SRC_URI = "git://github.com/rpm-software-management/rpm \
           file://0001-Do-not-add-an-unsatisfiable-dependency-when-building.patch \
           file://0001-Do-not-read-config-files-from-HOME.patch \
           file://0001-When-cross-installing-execute-package-scriptlets-wit.patch \
           file://0001-Do-not-reset-the-PATH-environment-variable-before-ru.patch \
           file://0002-Add-support-for-prefixing-etc-from-RPM_ETCCONFIGDIR-.patch \
           file://0001-When-nice-value-cannot-be-reset-issue-a-notice-inste.patch \
           file://0001-Do-not-hardcode-lib-rpm-as-the-installation-path-for.patch \
           file://0001-Fix-build-with-musl-C-library.patch \
           file://0001-Add-a-color-setting-for-mips64_n32-binaries.patch \
           file://0001-Add-PYTHON_ABI-when-searching-for-python-libraries.patch \
           "

PV = "4.13.90+git${SRCPV}"
PE = "1"
SRCREV = "a8e51b3bb05c6acb1d9b2e3d34f859ddda1677be"

S = "${WORKDIR}/git"

DEPENDS = "nss libarchive db file popt xz dbus elfutils python3"
DEPENDS_append_class-native = " file-replacement-native"

inherit autotools gettext pkgconfig python3native
export PYTHON_ABI

# OE-core patches autoreconf to additionally run gnu-configize, which fails with this recipe
EXTRA_AUTORECONF_append = " --exclude=gnu-configize"

EXTRA_OECONF_append = " --without-lua --enable-python"
EXTRA_OECONF_append_libc-musl = " --disable-nls"

# --sysconfdir prevents rpm from attempting to access machine-specific configuration in sysroot/etc; we need to have it in rootfs
#
# --localstatedir prevents rpm from writing its database to native sysroot when building images
#
# Also disable plugins, so that rpm doesn't attempt to inhibit shutdown via session dbus
EXTRA_OECONF_append_class-native = " --sysconfdir=/etc --localstatedir=/var --disable-plugins"

BBCLASSEXTEND = "native nativesdk"

# Direct rpm-native to read configuration from our sysroot, not the one it was compiled in
# libmagic also has sysroot path contamination, so override it
do_install_append_class-native() {
        create_wrapper ${D}/${bindir}/rpmbuild \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpmsign \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpmkeys \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpm \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpm2archive \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpm2cpio \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpmdb \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpmgraph \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1

        create_wrapper ${D}/${bindir}/rpmspec \
                RPM_CONFIGDIR=${STAGING_LIBDIR_NATIVE}/rpm \
                RPM_ETCCONFIGDIR=${STAGING_DIR_NATIVE} \
                MAGIC=${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc \
                RPM_NO_CHROOT_FOR_SCRIPTS=1
}


# Rpm's make install creates var/tmp which clashes with base-files packaging
do_install_append_class-target() {
    rm -rf ${D}/var
}

do_install_append () {
	sed -i -e 's:${HOSTTOOLS_DIR}/::g' ${D}/${libdir}/rpm/macros
}

FILES_${PN} += "${libdir}/rpm-plugins/*.so \
               "

FILES_${PN}-dev += "${libdir}/rpm-plugins/*.la \
                    "

PACKAGES += "python3-rpm"
PROVIDES += "python3-rpm"
FILES_python3-rpm = "${PYTHON_SITEPACKAGES_DIR}/rpm/*"

# rpm 5.x was packaging the rpm build tools separately
RPROVIDES_${PN} += "rpm-build"
