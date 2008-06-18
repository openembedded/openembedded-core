DEPENDS += " cmake-native "

# We want the staging and installing functions from autotools
inherit autotools

cmake_do_configure() {
     cmake . -DCMAKE_INSTALL_PREFIX:PATH=${prefix} -Wno-dev \
     -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_HOST}
}

EXPORT_FUNCTIONS do_configure
