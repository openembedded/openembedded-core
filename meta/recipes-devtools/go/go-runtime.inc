DEPENDS = "virtual/${TARGET_PREFIX}go go-native"
PROVIDES = "virtual/${TARGET_PREFIX}go-runtime"

export GOHOSTOS = "${BUILD_GOOS}"
export GOHOSTARCH = "${BUILD_GOARCH}"
export GOOS = "${TARGET_GOOS}"
export GOARCH = "${TARGET_GOARCH}"
export GOARM = "${TARGET_GOARM}"
export GOROOT_BOOTSTRAP = "${STAGING_LIBDIR_NATIVE}/go"
export GOROOT_FINAL = "${libdir}/go"
export GO_TARGET_INSTALL = "std"
export CGO_ENABLED = "1"
export CC_FOR_TARGET="${CC}"
export CXX_FOR_TARGET="${CXX}"
export GOROOT_OVERRIDE = "1"

do_configure[noexec] = "1"

do_compile() {
	export GOBIN="${B}/bin"
	export CC="${@d.getVar('BUILD_CC').strip()}"
	rm -rf ${GOBIN} ${B}/pkg
	mkdir ${GOBIN}
	cd src
	GO_FLAGS="" ./make.bash
	cd ${B}
}

do_install() {
	install -d ${D}${libdir}/go/src
	cp --preserve=mode,timestamps -R ${B}/pkg ${D}${libdir}/go/
	if [ "${BUILD_GOTUPLE}" != "${TARGET_GOTUPLE}" ]; then
		rm -rf ${D}${libdir}/go/pkg/${BUILD_GOTUPLE}
		rm -rf ${D}${libdir}/go/pkg/obj/${BUILD_GOTUPLE}
	fi
	rm -rf ${D}${libdir}/go/pkg/tool
	rm -rf ${D}${libdir}/go/pkg/obj
	rm -rf ${D}${libdir}/go/pkg/bootstrap
	find src -mindepth 1 -maxdepth 1 -type d | while read srcdir; do
		[ "$srcdir" = "./cmd" ] || cp --preserve=mode,timestamps -R $srcdir ${D}${libdir}/go/src/
	done
}

# These testdata directories aren't needed for builds and contain binaries
# that can cause errors in sysroot_strip(), so just remove them.
sysroot_stage_all_append() {
	find ${SYSROOT_DESTDIR}${libdir}/go/src -depth -type d -name 'testdata' -exec rm -rf {} \;
}

ALLOW_EMPTY_${PN} = "1"
FILES_${PN}-dev = "${libdir}/go/src ${libdir}/go/pkg/include"
FILES_${PN}-staticdev = "${libdir}/go/pkg/${TARGET_GOTUPLE} ${libdir}/go/pkg/${TARGET_GOTUPLE}"
# The testdata directories in the source tree include some binaries for various
# architectures, scripts, and .a files
INSANE_SKIP_${PN}-dev = "staticdev ldflags file-rdeps arch"

INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
