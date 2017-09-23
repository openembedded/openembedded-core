BUILD_GOOS = "${@go_map_os(d.getVar('BUILD_OS'), d)}"
BUILD_GOARCH = "${@go_map_arch(d.getVar('BUILD_ARCH'), d)}"
BUILD_GOTUPLE = "${BUILD_GOOS}_${BUILD_GOARCH}"
HOST_GOOS = "${@go_map_os(d.getVar('HOST_OS'), d)}"
HOST_GOARCH = "${@go_map_arch(d.getVar('HOST_ARCH'), d)}"
HOST_GOARM = "${@go_map_arm(d.getVar('HOST_ARCH'), d.getVar('TUNE_FEATURES'), d)}"
HOST_GOTUPLE = "${HOST_GOOS}_${HOST_GOARCH}"
TARGET_GOOS = "${@go_map_os(d.getVar('TARGET_OS'), d)}"
TARGET_GOARCH = "${@go_map_arch(d.getVar('TARGET_ARCH'), d)}"
TARGET_GOARM = "${@go_map_arm(d.getVar('TARGET_ARCH'), d.getVar('TUNE_FEATURES'), d)}"
TARGET_GOTUPLE = "${TARGET_GOOS}_${TARGET_GOARCH}"
GO_BUILD_BINDIR = "${@['bin/${HOST_GOTUPLE}','bin'][d.getVar('BUILD_GOTUPLE') == d.getVar('HOST_GOTUPLE')]}"

# Go supports dynamic linking on a limited set of architectures.
# See the supportsDynlink function in go/src/cmd/compile/internal/gc/main.go
GO_DYNLINK = ""
GO_DYNLINK_arm = "1"
GO_DYNLINK_aarch64 = "1"
GO_DYNLINK_x86 = "1"
GO_DYNLINK_x86-64 = "1"
GO_DYNLINK_powerpc64 = "1"
GO_DYNLINK_class-native = ""

# define here because everybody inherits this class
#
COMPATIBLE_HOST_linux-gnux32 = "null"
COMPATIBLE_HOST_linux-muslx32 = "null"
COMPATIBLE_HOST_powerpc = "null"
COMPATIBLE_HOST_powerpc64 = "null"
ARM_INSTRUCTION_SET = "arm"

def go_map_arch(a, d):
    import re
    if re.match('i.86', a):
        return '386'
    elif a == 'x86_64':
        return 'amd64'
    elif re.match('arm.*', a):
        return 'arm'
    elif re.match('aarch64.*', a):
        return 'arm64'
    elif re.match('mips64el*', a):
        return 'mips64le'
    elif re.match('mips64*', a):
        return 'mips64'
    elif re.match('mips.*', a):
        tf = d.getVar('TUNE_FEATURES').split()
        if 'fpu-hard' in tf and 'n32' not in tf:
            if 'mips32r2' in tf:
                newtf = [t for t in tf if t != 'mips32r2']
                newtf.append('mips32')
                d.setVar('TUNE_FEATURES', ' '.join(newtf))
            return 'mips' if 'bigendian' in tf else 'mipsle'
    elif re.match('p(pc|owerpc)(64)', a):
        return 'ppc64'
    elif re.match('p(pc|owerpc)(64el)', a):
        return 'ppc64le'
    else:
        raise bb.parse.SkipPackage("Unsupported CPU architecture: %s" % a)

def go_map_arm(a, f, d):
    import re
    if re.match('arm.*', a):
        if 'armv7' in f:
            return '7'
        elif 'armv6' in f:
            return '6'
        elif 'armv5' in f:
            return '5'
    return ''

def go_map_os(o, d):
    if o.startswith('linux'):
        return 'linux'
    return o


