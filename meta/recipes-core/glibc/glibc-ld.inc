inherit linuxloader

def ld_append_if_tune_exists(d, infos):
    loader = get_linuxloader(d)
    if loader:
        infos['ldconfig'].add('{"' + loader + '",' + "FLAG_ELF_LIBC6" + ' }')
        infos['lddrewrite'].add(loader)

def glibc_dl_info(d):
    infos = {'ldconfig':set(), 'lddrewrite':set()}
    ld_append_if_tune_exists(d, infos)

    #DEFAULTTUNE_MULTILIB_ORIGINAL
    original_tune=d.getVar("DEFAULTTUNE_MULTILIB_ORIGINAL")
    if original_tune:
        localdata = bb.data.createCopy(d)
        localdata.setVar("DEFAULTTUNE", original_tune)
        ld_append_if_tune_exists(localdata, infos)

    variants = d.getVar("MULTILIB_VARIANTS") or ""
    for item in variants.split():
        localdata = bb.data.createCopy(d)
        overrides = localdata.getVar("OVERRIDES", False) + ":virtclass-multilib-" + item
        localdata.setVar("OVERRIDES", overrides)
        ld_append_if_tune_exists(localdata, infos)
    infos['ldconfig'] = ','.join(infos['ldconfig'])
    infos['lddrewrite'] = ' '.join(infos['lddrewrite'])
    return infos

EGLIBC_KNOWN_INTERPRETER_NAMES = "${@glibc_dl_info(d)['ldconfig']}"
RTLDLIST = "${@glibc_dl_info(d)['lddrewrite']}"
RTLDLIST_class-nativesdk = "${base_libdir}/${@bb.utils.contains('SDK_ARCH', 'x86_64', 'ld-linux-x86-64.so.2', 'ld-linux.so.2', d)}"
glibc_dl_info[vardepsexclude] = "OVERRIDES"
