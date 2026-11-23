#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import json
import os

from oe.tune import riscv_isa_to_tune
from oeqa.selftest.case import OESelftestTestCase
from oeqa.utils.commands import bitbake, get_bb_var, get_bb_vars

class RiscvTunesTests(OESelftestTestCase):
    @classmethod
    def setUpClass(cls):
        super().setUpClass()
        cls.orig_machine = get_bb_var('MACHINE')

    def _machine_config(self, machine='qemuriscv64'):
        # Builds set up by bitbake-setup select the machine with a builtin
        # fragment, which is fatal alongside a MACHINE assignment, so drop it
        return 'OE_FRAGMENTS:remove = "machine/%s"\nMACHINE = "%s"\n' % (self.orig_machine, machine)

    def test_riscv_default_tunes(self):
        """
        Parse every RISC-V tune and check it yields a usable package arch, and
        that PACKAGE_EXTRA_ARCHS only lists archs of other tunes whose
        extensions are all available in this one.
        """
        # Only variables are checked here, so the riscv32 tunes can use the same
        # machine
        self.write_config(self._machine_config())
        tunes = get_bb_var('AVAILTUNES').split()

        vars = {}
        for tune in tunes:
            self.write_config(self._machine_config() + 'DEFAULTTUNE = "%s"\n' % tune)
            vars[tune] = get_bb_vars(['TUNE_FEATURES', 'TUNE_PKGARCH', 'PACKAGE_EXTRA_ARCHS', 'PACKAGE_ARCHS'])

        pkgarch_features = {v['TUNE_PKGARCH']: set(v['TUNE_FEATURES'].split()) for v in vars.values()}

        for tune, v in vars.items():
            with self.subTest(tune=tune):
                self.assertTrue(v['TUNE_FEATURES'], 'empty TUNE_FEATURES')
                self.assertTrue(v['TUNE_PKGARCH'], 'empty TUNE_PKGARCH')
                self.assertIn(v['TUNE_PKGARCH'], v['PACKAGE_ARCHS'].split())

                features = set(v['TUNE_FEATURES'].split())
                # za64rs is a stricter za128rs, which RVA20U64 requires
                if 'za64rs' in features:
                    features.add('za128rs')
                for arch in v['PACKAGE_EXTRA_ARCHS'].split():
                    self.assertIn(arch, pkgarch_features, '%s is not the package arch of any tune' % arch)
                    self.assertLessEqual(pkgarch_features[arch], features, '%s needs extensions %s lacks' % (arch, tune))
                # The tune's own package arch has the highest priority
                self.assertEqual(v['PACKAGE_EXTRA_ARCHS'].split()[-1], v['TUNE_PKGARCH'])

        # Profile tunes are named after the profile
        for profile in ['rva20u64', 'rva22u64', 'rva23u64']:
            with self.subTest(profile=profile):
                self.assertEqual(vars[profile]['TUNE_PKGARCH'], profile + '_zifencei')
                self.assertEqual(vars[profile]['TUNE_FEATURES'], riscv_isa_to_tune(profile) + ' zifencei')

        # The package archs of the existing tunes must not change
        self.assertEqual(vars['riscv64gc']['TUNE_PKGARCH'], 'riscv64imafdc_zicsr_zifencei')
        self.assertEqual(vars['riscv32gc']['TUNE_PKGARCH'], 'riscv32imafdc_zicsr_zifencei')

        # Legacy tune names behave exactly like the tunes they alias
        for alias, target in [('riscv64', 'riscv64gc'), ('riscv32', 'riscv32gc')]:
            with self.subTest(alias=alias):
                self.assertIn(alias, tunes)
                self.assertEqual(vars[alias], vars[target])

    def test_riscv_profile_ccargs(self):
        """
        Check the -march and -mabi options of the profile tunes.
        """
        for profile in ['rva20u64', 'rva22u64', 'rva23u64']:
            with self.subTest(profile=profile):
                self.write_config(self._machine_config() + 'DEFAULTTUNE = "%s"\n' % profile)
                ccargs = get_bb_var('TUNE_CCARGS').split()
                self.assertIn('-mabi=lp64d', ccargs)
                march = [a for a in ccargs if a.startswith('-march=')]
                self.assertEqual(len(march), 1, ccargs)
                exts = march[0][len('-march='):].split('_')
                self.assertTrue(exts[0].startswith('rv64imafdc'), exts[0])
                # Every multi-letter extension of the profile is passed on
                for f in riscv_isa_to_tune(profile).split() + ['zifencei']:
                    if len(f) > 1 and f not in ('rv', '64'):
                        self.assertIn(f, exts)

    def test_riscv_pkgarch_fallback(self):
        """
        Check that custom tunes which don't set RISCVPKGARCH:tune-<name> get
        TUNE_RISCV_PKGARCH as their package arch, and that the sanity checks
        reject it if that is too long.
        """
        def config(tune, features):
            return self._machine_config() + \
                   'AVAILTUNES += "%s"\n' \
                   'TUNE_FEATURES:tune-%s = "%s"\n' \
                   'PACKAGE_EXTRA_ARCHS:tune-%s = "${RISCVPKGARCH}"\n' \
                   'DEFAULTTUNE = "%s"\n' % (tune, tune, features, tune, tune)

        self.write_config(config('rv64imac-zicond', riscv_isa_to_tune('rv64imac_zicond')))
        self.assertEqual(get_bb_var('TUNE_PKGARCH'), 'riscv64imac_zicond')

        # Use 'bitbake -p' to parse, so that sanity checks are triggered
        self.write_config(config('rva22-zicond', riscv_isa_to_tune('rva22u64') + ' zicond'))
        result = bitbake('-p', ignore_status=True)
        self.assertNotEqual(result.status, 0)
        self.assertIn('for DEFAULTTUNE (rva22-zicond) is longer than 64 characters', result.output)

    def test_riscv_unusual_tunes(self):
        """
        Define custom tunes with unusual extension combinations, as a BSP
        might, and check each one builds: the sanity checks accept the
        package arch, gcc and binutils accept the -march string, and the
        package arch works in the build and sstate paths.
        """
        def profile(name, drop=None):
            return ' '.join(f for f in riscv_isa_to_tune(name).split() if f != drop) + ' zifencei'
        tunes = {
            # no float (lp64 ABI), with bitmanip. Short enough to use the
            # fallback package arch.
            'rv64-nofloat-bitmanip': riscv_isa_to_tune('rv64imac_zicsr_zifencei_zba_zbb_zbs'),
            # no profile, scalar extensions on top of gc
            'rv64gc-scalar-extras': riscv_isa_to_tune('rv64gc_zicbom_zicboz_zicond_zfa_zfhmin'),
            # profile plus vector crypto extensions
            'rva20-vector-crypto': profile('rva20u64') + ' v zvbb zvkt',
            # a profile missing one extension
            'rva22-no-zkt': profile('rva22u64', 'zkt'),
            # rva23u64 without the full vector extension
            'rva23-no-v': profile('rva23u64', 'v'),
        }

        config = self._machine_config()
        for tune, features in tunes.items():
            config += 'AVAILTUNES += "%s"\n' % tune
            config += 'TUNE_FEATURES:tune-%s = "%s"\n' % (tune, features)
            # The rest are too long for the fallback, so name them as a BSP
            # would have to
            if tune != 'rv64-nofloat-bitmanip':
                config += 'RISCVPKGARCH:tune-%s = "%s"\n' % (tune, tune.replace('-', '_'))
            config += 'PACKAGE_EXTRA_ARCHS:tune-%s = "${RISCVPKGARCH}"\n' % tune

        for tune in tunes:
            with self.subTest(tune=tune):
                self.write_config(config + 'DEFAULTTUNE = "%s"\n' % tune)
                # small, and compiled with TUNE_CCARGS. It has no do_build, and
                # populate_sysroot also writes an sstate object.
                bitbake('libgcc-initial -c populate_sysroot')

    def test_riscv_rust_target_features(self):
        """
        Check that the Rust target spec passes every extension in the tune's
        TUNE_FEATURES on to LLVM, in TUNE_FEATURES order.
        """
        for tune in ['riscv64gc', 'rva23u64']:
            with self.subTest(tune=tune):
                self.write_config(self._machine_config() + 'DEFAULTTUNE = "%s"\n' % tune)
                # hello-rs is a small cargo recipe in meta-selftest, and
                # do_rust_gen_targets runs right after do_patch, so neither
                # the toolchain nor Rust has to be built
                bitbake('hello-rs -c rust_gen_targets')
                v = get_bb_vars(['WORKDIR', 'RUST_TARGET_SYS', 'TUNE_FEATURES'], 'hello-rs')
                with open(os.path.join(v['WORKDIR'], 'rust-targets', v['RUST_TARGET_SYS'] + '.json')) as f:
                    features = json.load(f)['features'].split(',')

                # Everything but the base ISA and its width
                expected = ['+' + e for e in v['TUNE_FEATURES'].split() if e not in ('rv', '64', 'i')]
                self.assertEqual(features, expected)
                if tune == 'riscv64gc':
                    # The features of the existing tunes must not change
                    self.assertEqual(features, ['+m', '+a', '+f', '+d', '+c', '+zicsr', '+zifencei'])
