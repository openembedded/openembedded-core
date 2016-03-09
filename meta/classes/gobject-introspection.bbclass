# Inherit this class in recipes to enable building their introspection files

# This allows disabling introspection support in recipes
# (and therefore avoiding the use of qemu)
# if gobject-introspection-data is omitted from DISTRO_FEATURES and MACHINE_FEATURES.
EXTRA_OECONF_prepend = "${@bb.utils.contains('COMBINED_FEATURES', 'gobject-introspection-data', '--enable-introspection', '--disable-introspection', d)} "

UNKNOWN_CONFIGURE_WHITELIST_append = " --enable-introspection --disable-introspection"

# Generating introspection data depends on a combination of native and target introspection tools,
# and qemu to run the target tools.
DEPENDS_append = " gobject-introspection gobject-introspection-native qemu-native"

# This is necessary for python scripts to succeed - distutils
# failes if these are not set
export BUILD_SYS
export HOST_SYS
export STAGING_LIBDIR
export STAGING_INCDIR

# This is used by introspection tools to find .gir includes
export XDG_DATA_DIRS = "${STAGING_DATADIR}"

do_configure_prepend_class-target () {
    # introspection.m4 pre-packaged with upstream tarballs does not yet
    # have our fixes
    mkdir -p ${S}/m4
    cp ${STAGING_DIR_TARGET}/${datadir}/aclocal/introspection.m4 ${S}/m4
}


# .typelib files are needed at runtime and so they go to the main package
# (so they'll be together with libraries they support).
FILES_${PN}_append = " ${libdir}/girepository-*/*.typelib" 
    
# .gir files go to dev package, as they're needed for developing (but not for running)
# things that depends on introspection.
FILES_${PN}-dev_append = " ${datadir}/gir-*/*.gir"

