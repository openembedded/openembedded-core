def eglibc_cfg(feature, features, tokens, cnf):
    if type(tokens) == type(""):
        tokens = [tokens]
    if type(features) == type([]) and feature in features:
        cnf.extend([token + ' = y' for token in tokens])
    else:
        for token in tokens:
            cnf.extend([token + ' = n'])
            if token == 'OPTION_EGLIBC_NSSWITCH':
                cnf.extend(["OPTION_EGLIBC_NSSWITCH_FIXED_CONFIG = ${S}/nss/nsswitch.conf"])
                cnf.extend(["OPTION_EGLIBC_NSSWITCH_FIXED_FUNCTIONS = ${S}/nss/fixed-nsswitch.functions"])

# arrange the dependencies among eglibc configuable options according to file option-groups.def from eglibc source code
def distro_features_check_deps(distro_features):
    new_dep = True
    while new_dep:
        new_dep = False

        if 'ipv6' in distro_features and 'ipv4' not in distro_features:
            new_dep = True
            distro_features.extend(['ipv4'])

        if 'ipv4' in distro_features and 'libc-nsswitch' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-nsswitch'])

        if 'libc-cxx-tests' in distro_features:
            if 'libc-posix-wchar-io' not in distro_features:
                new_dep = True
                distro_features.extend(['libc-posix-wchar-io'])
            if 'libc-libm' not in distro_features:
                new_dep = True
                distro_features.extend(['libc-libm'])

        if 'libc-catgets' in distro_features and 'libc-locale-code' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-locale-code'])

        if 'libc-crypt-ufc' in distro_features and 'libc-crypt' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-crypt'])

        if 'libc-getlogin' in distro_features and 'libc-utmp' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-utmp'])

        if 'libc-inet-anl' in distro_features and 'ipv4' not in distro_features:
            new_dep = True
            distro_features.extend(['ipv4'])

        if 'libc-locale-code' in distro_features and 'libc-posix-clang-wchar' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-posix-clang-wchar'])

        if 'libc-nis' in distro_features:
            if 'ipv4' not in distro_features:
                new_dep = True
                distro_features.extend(['ipv4'])
            if 'libc-sunrpc' not in distro_features:
                new_dep = True
                distro_features.extend(['libc-sunrpc'])

        if 'libc-rcmd' in distro_features and 'ipv4' not in distro_features:
            new_dep = True
            distro_features.extend(['ipv4'])

        if 'libc-sunrpc' in distro_features and 'ipv4' not in distro_features:
            new_dep = True
            distro_features.extend(['ipv4'])

        if 'libc-utmpx' in distro_features and 'libc-utmp' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-utmp'])

        if 'libc-posix-regexp-glibc' in distro_features and 'libc-posix-regexp' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-posix-regexp'])

        if 'libc-posix-wchar-io' in distro_features and 'libc-posix-clang-wchar' not in distro_features:
            new_dep = True
            distro_features.extend(['libc-posix-clang-wchar'])

# Map distro features to eglibc options settings
def features_to_eglibc_settings(d):
        cnf = ([])
        distro_features = (d.getVar('DISTRO_FEATURES', True) or '').split()

        distro_features_check_deps(distro_features)

        eglibc_cfg('ipv6',      distro_features, 'OPTION_EGLIBC_ADVANCED_INET6', cnf)
        eglibc_cfg('libc-backtrace',      distro_features, 'OPTION_EGLIBC_BACKTRACE', cnf)
        eglibc_cfg('libc-big-macros',      distro_features, 'OPTION_EGLIBC_BIG_MACROS', cnf)
        eglibc_cfg('libc-bsd',      distro_features, 'OPTION_EGLIBC_BSD', cnf)
        eglibc_cfg('libc-cxx-tests',      distro_features, 'OPTION_EGLIBC_CXX_TESTS', cnf)
        eglibc_cfg('libc-catgets',      distro_features, 'OPTION_EGLIBC_CATGETS', cnf)
        eglibc_cfg('libc-charsets',      distro_features, 'OPTION_EGLIBC_CHARSETS', cnf)
        eglibc_cfg('libc-crypt',      distro_features, 'OPTION_EGLIBC_CRYPT', cnf)
        eglibc_cfg('libc-crypt-ufc',      distro_features, 'OPTION_EGLIBC_CRYPT_UFC', cnf)
        eglibc_cfg('libc-db-aliases',      distro_features, 'OPTION_EGLIBC_DB_ALIASES', cnf)
        eglibc_cfg('libc-envz',      distro_features, 'OPTION_EGLIBC_ENVZ', cnf)
        eglibc_cfg('libc-fcvt',      distro_features, 'OPTION_EGLIBC_FCVT', cnf)
        eglibc_cfg('libc-fmtmsg',      distro_features, 'OPTION_EGLIBC_FMTMSG', cnf)
        eglibc_cfg('libc-fstab',      distro_features, 'OPTION_EGLIBC_FSTAB', cnf)
        eglibc_cfg('libc-ftraverse',      distro_features, 'OPTION_EGLIBC_FTRAVERSE', cnf)
        eglibc_cfg('libc-getlogin',      distro_features, 'OPTION_EGLIBC_GETLOGIN', cnf)
        eglibc_cfg('libc-idn',      distro_features, 'OPTION_EGLIBC_IDN', cnf)
        eglibc_cfg('ipv4',      distro_features, 'OPTION_EGLIBC_INET', cnf)
        eglibc_cfg('libc-inet-anl',      distro_features, 'OPTION_EGLIBC_INET_ANL', cnf)
        eglibc_cfg('libc-libm',      distro_features, 'OPTION_EGLIBC_LIBM', cnf)
        eglibc_cfg('libc-libm-big',      distro_features, 'OPTION_EGLIBC_LIBM_BIG', cnf)
        eglibc_cfg('libc-locales',      distro_features, 'OPTION_EGLIBC_LOCALES', cnf)
        eglibc_cfg('libc-locale-code',      distro_features, 'OPTION_EGLIBC_LOCALE_CODE', cnf)
        eglibc_cfg('libc-memusage',      distro_features, 'OPTION_EGLIBC_MEMUSAGE', cnf)
        eglibc_cfg('libc-nis',      distro_features, 'OPTION_EGLIBC_NIS', cnf)
        eglibc_cfg('libc-nsswitch',      distro_features, 'OPTION_EGLIBC_NSSWITCH', cnf)
        eglibc_cfg('libc-rcmd',      distro_features, 'OPTION_EGLIBC_RCMD', cnf)
        eglibc_cfg('libc-rtld-debug',      distro_features, 'OPTION_EGLIBC_RTLD_DEBUG', cnf)
        eglibc_cfg('libc-spawn',      distro_features, 'OPTION_EGLIBC_SPAWN', cnf)
        eglibc_cfg('libc-streams',      distro_features, 'OPTION_EGLIBC_STREAMS', cnf)
        eglibc_cfg('libc-sunrpc',      distro_features, 'OPTION_EGLIBC_SUNRPC', cnf)
        eglibc_cfg('libc-utmp',      distro_features, 'OPTION_EGLIBC_UTMP', cnf)
        eglibc_cfg('libc-utmpx',      distro_features, 'OPTION_EGLIBC_UTMPX', cnf)
        eglibc_cfg('libc-wordexp',      distro_features, 'OPTION_EGLIBC_WORDEXP', cnf)
        eglibc_cfg('libc-posix-clang-wchar',      distro_features, 'OPTION_POSIX_C_LANG_WIDE_CHAR', cnf)
        eglibc_cfg('libc-posix-regexp',      distro_features, 'OPTION_POSIX_REGEXP', cnf)
        eglibc_cfg('libc-posix-regexp-glibc',      distro_features, 'OPTION_POSIX_REGEXP_GLIBC', cnf)
        eglibc_cfg('libc-posix-wchar-io',      distro_features, 'OPTION_POSIX_WIDE_CHAR_DEVICE_IO', cnf)

        return "\n".join(cnf)
