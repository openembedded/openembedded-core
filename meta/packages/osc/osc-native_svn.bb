DESCRIPTION = "osc - OpenSUSE build service command-line tool."
HOMEPAGE = "http://en.opensuse.org/Build_Service/CLI"
SECTION = "console/utils"
LICENSE = "GPL"
PV  = "0.0+svnr${SRCREV}"
PR = "r0"
DEPENDS="python-native rpm-native"
RDEPENDS=""

SRC_URI = "svn://forgesvn1.novell.com/svn/opensuse/trunk/buildservice/src/clientlib/python;module=osc;proto=https"
S = "${WORKDIR}/osc"

inherit native

do_stage() {
        BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE} STAGING_INCDIR=${STAGING_INCDIR_NATIVE} \
        ${STAGING_BINDIR_NATIVE}/python setup.py build ||
        oefatal "python setup.py build execution failed."

        BUILD_SYS=${BUILD_SYS} HOST_SYS=${HOST_SYS} \
        STAGING_LIBDIR=${STAGING_LIBDIR_NATIVE} STAGING_INCDIR=${STAGING_INCDIR_NATIVE} \
        ${STAGING_BINDIR_NATIVE}/python setup.py install --prefix=${STAGING_BINDIR_NATIVE}/.. --install-data=${STAGING_DATADIR_NATIVE} || \
        oefatal "python setup.py install execution failed."

        ln -sf ${STAGING_BINDIR_NATIVE}/osc-wrapper.py ${STAGING_BINDIR_NATIVE}/osc
}

do_install() {
        :
}
