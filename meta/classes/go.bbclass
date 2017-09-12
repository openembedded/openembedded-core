inherit goarch

def get_go_parallel_make(d):
    pm = (d.getVar('PARALLEL_MAKE') or '').split()
    # look for '-j' and throw other options (e.g. '-l') away
    # because they might have a different meaning in golang
    while pm:
        opt = pm.pop(0)
        if opt == '-j':
            v = pm.pop(0)
        elif opt.startswith('-j'):
            v = opt[2:].strip()
        else:
            continue

        return '-p %d' % int(v)

    return ""

GO_PARALLEL_BUILD ?= "${@get_go_parallel_make(d)}"

GOROOT_class-native = "${STAGING_LIBDIR_NATIVE}/go"
GOROOT = "${STAGING_LIBDIR}/go"
GOBIN_FINAL_class-native = "${GOROOT_FINAL}/bin"
GOBIN_FINAL = "${GOROOT_FINAL}/${GO_BUILD_BINDIR}"

DEPENDS_GOLANG_class-target = "virtual/${TARGET_PREFIX}go virtual/${TARGET_PREFIX}go-runtime"
DEPENDS_GOLANG_class-native = "go-native"

DEPENDS_append = " ${DEPENDS_GOLANG}"

export GOBUILDFLAGS ?= "-v"
GOBUILDFLAGS_prepend_task-compile = "${GO_PARALLEL_BUILD} "

export GO = "${HOST_PREFIX}go"
GOTOOLDIR = "${STAGING_LIBDIR_NATIVE}/${TARGET_SYS}/go/pkg/tool/${BUILD_GOTUPLE}"
GOTOOLDIR_class-native = "${STAGING_LIBDIR_NATIVE}/go/pkg/tool/${BUILD_GOTUPLE}"
export GOTOOLDIR
export CGO_ENABLED = "1"
export GOROOT
export GOROOT_FINAL = "${libdir}/go"
export GOBIN_FINAL
export GOPKG_FINAL = "${GOROOT_FINAL}/pkg/${TARGET_GOTUPLE}"
export GOSRC_FINAL = "${GOROOT_FINAL}/src"
export GO_GCFLAGS = "${TARGET_CFLAGS}"
export GO_LDFLAGS = "${TARGET_LDFLAGS}"
export CGO_CFLAGS = "${TARGET_CC_ARCH}${TOOLCHAIN_OPTIONS} ${TARGET_CFLAGS}"
export CGO_CPPFLAGS = "${TARGET_CPPFLAGS}"
export CGO_CXXFLAGS = "${TARGET_CC_ARCH}${TOOLCHAIN_OPTIONS} ${TARGET_CXXFLAGS}"
export CGO_LDFLAGS = "${TARGET_CC_ARCH}${TOOLCHAIN_OPTIONS} ${TARGET_LDFLAGS}"

FILES_${PN}-staticdev += "${GOSRC_FINAL}/${GO_IMPORT}"
FILES_${PN}-staticdev += "${GOPKG_FINAL}/${GO_IMPORT}*"

GO_INSTALL ?= "${GO_IMPORT}/..."
GO_INSTALL_FILTEROUT ?= "${GO_IMPORT}/vendor/"

B = "${WORKDIR}/build"
export GOPATH = "${B}"

python go_do_unpack() {
    src_uri = (d.getVar('SRC_URI') or "").split()
    if len(src_uri) == 0:
        return

    try:
        fetcher = bb.fetch2.Fetch(src_uri, d)
        for url in fetcher.urls:
            if fetcher.ud[url].type == 'git':
                if fetcher.ud[url].parm.get('destsuffix') is None:
                    s_dirname = os.path.basename(d.getVar('S'))
                    fetcher.ud[url].parm['destsuffix'] = os.path.join(s_dirname, 'src',
                                                                      d.getVar('GO_IMPORT')) + '/'
        fetcher.unpack(d.getVar('WORKDIR'))
    except bb.fetch2.BBFetchException as e:
        raise bb.build.FuncFailed(e)
}

go_list_packages() {
	${GO} list -f '{{.ImportPath}}' ${GOBUILDFLAGS} ${GO_INSTALL} | \
		egrep -v '${GO_INSTALL_FILTEROUT}'
}

go_do_configure() {
	ln -snf ${S}/src ${B}/
}

go_do_compile() {
	${GO} env
	if [ -n "${GO_INSTALL}" ]; then
		${GO} install ${GOBUILDFLAGS} `go_list_packages`
	fi
}
do_compile[cleandirs] = "${B}/bin ${B}/pkg"

go_do_install() {
	install -d ${D}${GOROOT_FINAL}/src/${GO_IMPORT}
	tar -C ${S}/src/${GO_IMPORT} -cf - --exclude-vcs . | \
		tar -C ${D}${GOROOT_FINAL}/src/${GO_IMPORT} --no-same-owner -xf -
	tar -C ${B} -cf - pkg | tar -C ${D}${GOROOT_FINAL} --no-same-owner -xf -

	if [ -n "`ls ${B}/${GO_BUILD_BINDIR}/`" ]; then
		install -d ${D}${bindir}
		install -m 0755 ${B}/${GO_BUILD_BINDIR}/* ${D}${bindir}/
	fi
}

EXPORT_FUNCTIONS do_unpack do_configure do_compile do_install
