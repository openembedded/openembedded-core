DEPENDS += " cmake-native "

# We need to unset CCACHE otherwise cmake gets too confused
CCACHE = ""

# We want the staging and installing functions from autotools
inherit autotools

# Use in-tree builds by default but allow this to be changed
# since some packages do not support them (e.g. llvm 2.5).
OECMAKE_SOURCEPATH ?= "."

# If declaring this, make sure you also set EXTRA_OEMAKE to
# "-C ${OECMAKE_BUILDPATH}". So it will run the right makefiles.
OECMAKE_BUILDPATH ?= ""

# C/C++ Compiler (without cpu arch/tune arguments)
OECMAKE_C_COMPILER ?= "`echo ${CC} | sed 's/^\([^ ]*\).*/\1/'`"
OECMAKE_CXX_COMPILER ?= "`echo ${CXX} | sed 's/^\([^ ]*\).*/\1/'`"

# Compiler flags
OECMAKE_C_FLAGS ?= "${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} ${CPPFLAGS}"
OECMAKE_CXX_FLAGS ?= "${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} ${CXXFLAGS} -fpermissive"
OECMAKE_C_FLAGS_RELEASE ?= "${SELECTED_OPTIMIZATION} ${CPPFLAGS} -DNDEBUG"
OECMAKE_CXX_FLAGS_RELEASE ?= "${SELECTED_OPTIMIZATION} ${CXXFLAGS} -DNDEBUG"

OECMAKE_RPATH ?= ""
OECMAKE_PERLNATIVE_DIR ??= ""

cmake_do_generate_toolchain_file() {
	cat > ${WORKDIR}/toolchain.cmake <<EOF
# CMake system name must be something like "Linux".
# This is important for cross-compiling.
set( CMAKE_SYSTEM_NAME `echo ${SDK_OS} | sed 's/^./\u&/'` )
set( CMAKE_SYSTEM_PROCESSOR ${TARGET_ARCH} )
set( CMAKE_C_COMPILER ${OECMAKE_C_COMPILER} )
set( CMAKE_CXX_COMPILER ${OECMAKE_CXX_COMPILER} )
set( CMAKE_C_FLAGS "${OECMAKE_C_FLAGS}" CACHE STRING "CFLAGS" )
set( CMAKE_CXX_FLAGS "${OECMAKE_CXX_FLAGS}" CACHE STRING "CXXFLAGS" )
set( CMAKE_C_FLAGS_RELEASE "${OECMAKE_C_FLAGS_RELEASE}" CACHE STRING "CFLAGS for release" )
set( CMAKE_CXX_FLAGS_RELEASE "${OECMAKE_CXX_FLAGS_RELEASE}" CACHE STRING "CXXFLAGS for release" )

# only search in the paths provided so cmake doesnt pick
# up libraries and tools from the native build machine
set( CMAKE_FIND_ROOT_PATH ${STAGING_DIR_HOST} ${STAGING_DIR_NATIVE} ${CROSS_DIR} ${OECMAKE_PERLNATIVE_DIR})
set( CMAKE_FIND_ROOT_PATH_MODE_PROGRAM ONLY )
set( CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY )
set( CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY )

# Use qt.conf settings
set( ENV{QT_CONF_PATH} ${WORKDIR}/qt.conf )

# We need to set the rpath to the correct directory as cmake does not provide any
# directory as rpath by default
set( CMAKE_INSTALL_RPATH ${OECMAKE_RPATH} )

# Use native cmake modules
set( CMAKE_MODULE_PATH ${STAGING_DATADIR}/cmake/Modules/ )

# add for non /usr/lib libdir, e.g. /usr/lib64
set( CMAKE_LIBRARY_PATH ${libdir} )

EOF
}

addtask generate_toolchain_file after do_patch before do_configure

cmake_do_configure() {
	if [ ${OECMAKE_BUILDPATH} ]
	then
		mkdir -p ${OECMAKE_BUILDPATH}
		cd ${OECMAKE_BUILDPATH}
	fi

	# Just like autotools cmake can use a site file to cache result that need generated binaries to run
	if [ -e ${WORKDIR}/site-file.cmake ] ; then
		OECMAKE_SITEFILE=" -C ${WORKDIR}/site-file.cmake"
	else
		OECMAKE_SITEFILE=""
	fi

	cmake \
	  ${OECMAKE_SITEFILE} \
	  ${OECMAKE_SOURCEPATH} \
	  -DCMAKE_INSTALL_PREFIX:PATH=${prefix} \
	  -DCMAKE_INSTALL_SO_NO_EXE=0 \
	  -DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain.cmake \
	  -DCMAKE_VERBOSE_MAKEFILE=1 \
	  ${EXTRA_OECMAKE} \
	  -Wno-dev
}

cmake_do_compile()  {
	if [ ${OECMAKE_BUILDPATH} ]
	then
		cd ${OECMAKE_BUILDPATH}
	fi

	base_do_compile
}

cmake_do_install() {
	if [ ${OECMAKE_BUILDPATH} ];
	then
		cd ${OECMAKE_BUILDPATH}
	fi

	autotools_do_install
}

EXPORT_FUNCTIONS do_configure do_compile do_install do_generate_toolchain_file
