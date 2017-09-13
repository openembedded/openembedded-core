inherit cross-canadian

DEPENDS = "go-native virtual/${HOST_PREFIX}gcc-crosssdk virtual/nativesdk-${HOST_PREFIX}libc-for-gcc virtual/nativesdk-${HOST_PREFIX}compilerlibs"
PN = "go-cross-canadian-${TRANSLATED_TARGET_ARCH}"

export GOHOSTOS = "${BUILD_GOOS}"
export GOHOSTARCH = "${BUILD_GOARCH}"
export GOOS = "${HOST_GOOS}"
export GOARCH = "${HOST_GOARCH}"
export GOARM = "${HOST_GOARM}"
export GOROOT_BOOTSTRAP = "${STAGING_LIBDIR_NATIVE}/go"
export GOTOOLDIR_BOOTSTRAP = "${GOROOT_BOOTSTRAP}/pkg/tool/${BUILD_GOTUPLE}"
export GOROOT_FINAL = "${libdir}/go"
export CGO_ENABLED = "1"
export CC_FOR_TARGET = "${HOST_CC}"
export CXX_FOR_TARGET = "${HOST_CXX}"
CC = "${@d.getVar('BUILD_CC', True).strip()}"
export GO_LDFLAGS = '-linkmode external -extld ${HOST_PREFIX}gcc -extldflags "${TOOLCHAIN_OPTIONS} ${LDFLAGS}"'

do_configure[noexec] = "1"

do_compile() {
	export GOBIN="${B}/bin"
	rm -rf ${GOBIN} ${B}/pkg
	mkdir ${GOBIN}
	cd src
	./make.bash --host-only --no-banner
	cd ${B}
}


make_wrapper() {
    rm -f ${D}${bindir}/$2
    cat <<END >${D}${bindir}/$2
#!/bin/bash
here=\`dirname \$0\`
native_goroot=\`readlink -f \$here/../../lib/${TARGET_SYS}/go\`
export GOARCH="${TARGET_GOARCH}"
export GOOS="${TARGET_GOOS}"
export GOARM="\${GOARM:-${TARGET_GOARM}}"
export GOTOOLDIR="\$native_goroot/pkg/tool/${HOST_GOTUPLE}"
export GOROOT="\${GOROOT:-\$OECORE_TARGET_SYSROOT/${target_libdir}/go}"
\$here/../../lib/${TARGET_SYS}/go/bin/$1 "\$@"
END
    chmod +x ${D}${bindir}/$2
}

do_install() {
	install -d ${D}${libdir}/go
	cp --preserve=mode,timestamps -R ${B}/pkg ${D}${libdir}/go/
	install -d ${D}${libdir}/go/src
	(cd ${S}/src; for d in *; do \
		[ -d $d ] && cp --preserve=mode,timestamps -R ${S}/src/$d ${D}${libdir}/go/src/; \
	done)
	rm -rf ${D}${libdir}/go/src/runtime/pprof/testdata
	install -d ${D}${bindir} ${D}${libdir}/go/bin
	for f in ${B}/bin/*
	do
		base=`basename $f`
		install -m755 $f ${D}${libdir}/go/bin
		make_wrapper $base ${TARGET_PREFIX}$base
	done
}

FILES_${PN}-staticdev = "${libdir}/go/pkg"
