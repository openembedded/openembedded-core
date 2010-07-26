DEPENDS += " cmake-native "

# We want the staging and installing functions from autotools
inherit autotools

EXTRA_OECMAKE ?= ""

cmake_do_configure() {
     cmake . -DCMAKE_INSTALL_PREFIX:PATH=${prefix} -Wno-dev \
     -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_HOST} \
     -DCMAKE_FIND_ROOT_PATH_MODE_INCLUDE=ONLY \
     -DCMAKE_FIND_ROOT_PATH_MODE_LIBRARY=ONLY \
     ${EXTRA_OECMAKE}
}

EXPORT_FUNCTIONS do_configure
