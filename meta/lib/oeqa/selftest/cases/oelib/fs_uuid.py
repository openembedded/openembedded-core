#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#
# Coverage for meta/classes-recipe/fs-uuid.bbclass (bug 15020).
# AI-Generated: Cursor Grok 4.6
#
# The helpers live in a bbclass rather than meta/lib/oe, so these tests load
# that file and run the functions with a fake bitbake 'bb' module and a mocked
# tune2fs. That is the same approach as other oelib tests: no image build, but
# the production parser and placeholder replacement are exercised directly.
#

import os
import shutil
import subprocess
import tempfile
import types
import unittest
from unittest.case import TestCase
from unittest.mock import patch

import oe


UUID_PLACEHOLDER = '<<uuid-of-rootfs>>'
SAMPLE_UUID = '5a3c2b1d-4e6f-7890-abcd-ef1234567890'
SAMPLE_ROOTFS = '/not/a/real/rootfs.ext4'


class BBFatal(Exception):
    """Stand-in for bb.fatal(), which logs and does not return."""


class FakeBB:
    def __init__(self):
        self.notes = []

    def note(self, msg):
        self.notes.append(msg)

    def fatal(self, msg):
        raise BBFatal(msg)


class FakeDataStore:
    def __init__(self, rootfs=SAMPLE_ROOTFS):
        self.rootfs = rootfs

    def getVar(self, name):
        if name != 'ROOTFS':
            raise KeyError(name)
        return self.rootfs


def _bbclass_path():
    # meta/lib/oe/__init__.py -> meta/classes-recipe/fs-uuid.bbclass
    meta_dir = os.path.dirname(os.path.dirname(os.path.dirname(oe.__file__)))
    return os.path.join(meta_dir, 'classes-recipe', 'fs-uuid.bbclass')


def _tune2fs_output(uuid, extra_before=None, extra_after=None, uuid_line=None):
    """A realistic `tune2fs -l` dump. Only the UUID line is required by the class."""
    lines = [
        'tune2fs 1.47.2 (1-Jan-2025)',
        'Filesystem volume name:   <none>',
        'Last mounted on:          <not available>',
    ]
    if extra_before:
        lines.extend(extra_before)
    if uuid_line is None:
        uuid_line = 'Filesystem UUID:          %s' % uuid
    if uuid_line is not False:
        lines.append(uuid_line)
    lines.extend([
        'Filesystem magic number:  0xEF53',
        'Filesystem revision #:    1 (dynamic)',
        'Filesystem features:      ext_attr',
        'Filesystem state:         clean',
    ])
    if extra_after:
        lines.extend(extra_after)
    return '\n'.join(lines) + '\n'


def _load_fs_uuid(bb_mod):
    path = _bbclass_path()
    with open(path, 'r', encoding='utf-8') as f:
        source = f.read()
    ns = {'__name__': 'fs_uuid_bbclass', 'bb': bb_mod}
    exec(compile(source, path, 'exec'), ns)
    return ns['get_rootfs_uuid'], ns['replace_rootfs_uuid']


class FsUuidTestCase(TestCase):
    """Shared loader so each test runs the functions from the live bbclass."""

    @classmethod
    def setUpClass(cls):
        cls.bb = FakeBB()
        get_rootfs_uuid, replace_rootfs_uuid = _load_fs_uuid(cls.bb)
        # Keep these off the TestCase class: a raw function stored there
        # becomes a bound method and would swallow the datastore argument.
        cls.fns = types.SimpleNamespace(
            get_rootfs_uuid=get_rootfs_uuid,
            replace_rootfs_uuid=replace_rootfs_uuid,
        )

    def setUp(self):
        self.bb.notes.clear()
        self.d = FakeDataStore()


class TestGetRootfsUuid(FsUuidTestCase):

    def test_parses_typical_tune2fs_output(self):
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output) as mock:
            uuid = self.fns.get_rootfs_uuid(self.d)
        self.assertEqual(uuid, SAMPLE_UUID)
        mock.assert_called_once_with(['tune2fs', '-l', SAMPLE_ROOTFS], text=True)

    def test_uses_rootfs_variable(self):
        other = '/work/tmp/foo.ext4'
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output) as mock:
            self.fns.get_rootfs_uuid(FakeDataStore(other))
        mock.assert_called_once_with(['tune2fs', '-l', other], text=True)

    def test_uuid_is_last_token_on_the_line(self):
        # tune2fs pads the label with spaces; the class uses split()[-1].
        line = 'Filesystem UUID:\t\t   %s' % SAMPLE_UUID
        output = _tune2fs_output(SAMPLE_UUID, uuid_line=line)
        with patch('subprocess.check_output', return_value=output):
            self.assertEqual(self.fns.get_rootfs_uuid(self.d), SAMPLE_UUID)

    def test_first_uuid_line_wins(self):
        later = 'ffffffff-ffff-ffff-ffff-ffffffffffff'
        output = _tune2fs_output(
            SAMPLE_UUID,
            extra_after=['Filesystem UUID:          %s' % later],
        )
        with patch('subprocess.check_output', return_value=output):
            self.assertEqual(self.fns.get_rootfs_uuid(self.d), SAMPLE_UUID)

    def test_notes_path_and_uuid(self):
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            self.fns.get_rootfs_uuid(self.d)
        self.assertEqual(self.bb.notes, ['UUID of %s: %s' % (SAMPLE_ROOTFS, SAMPLE_UUID)])

    def test_fatal_when_uuid_line_missing(self):
        output = _tune2fs_output(SAMPLE_UUID, uuid_line=False)
        with patch('subprocess.check_output', return_value=output):
            with self.assertRaisesRegex(BBFatal, SAMPLE_ROOTFS):
                self.fns.get_rootfs_uuid(self.d)

    def test_fatal_on_empty_tune2fs_output(self):
        with patch('subprocess.check_output', return_value=''):
            with self.assertRaisesRegex(BBFatal, 'Could not determine filesystem UUID'):
                self.fns.get_rootfs_uuid(self.d)

    def test_ignores_uuid_text_that_is_not_at_line_start(self):
        # startswith('Filesystem UUID:') is the contract; a mention later on a
        # line must not be treated as the UUID field.
        output = _tune2fs_output(
            SAMPLE_UUID,
            uuid_line=False,
            extra_after=['Default mount options:    Filesystem UUID: ignored'],
        )
        with patch('subprocess.check_output', return_value=output):
            with self.assertRaises(BBFatal):
                self.fns.get_rootfs_uuid(self.d)

    def test_tune2fs_failure_propagates(self):
        # Non-ext images (or missing e2fsprogs) fail at tune2fs; the class
        # does not catch CalledProcessError.
        err = subprocess.CalledProcessError(
            1, ['tune2fs', '-l', SAMPLE_ROOTFS], output='Bad magic number in super-block'
        )
        with patch('subprocess.check_output', side_effect=err):
            with self.assertRaises(subprocess.CalledProcessError):
                self.fns.get_rootfs_uuid(self.d)

    def test_missing_tune2fs_propagates(self):
        with patch('subprocess.check_output', side_effect=FileNotFoundError('tune2fs')):
            with self.assertRaises(FileNotFoundError):
                self.fns.get_rootfs_uuid(self.d)


class TestReplaceRootfsUuid(FsUuidTestCase):

    def test_no_placeholder_is_noop(self):
        original = 'root=/dev/sda2 rw'
        with patch('subprocess.check_output') as mock:
            result = self.fns.replace_rootfs_uuid(self.d, original)
        self.assertIs(result, original)
        mock.assert_not_called()
        self.assertEqual(self.bb.notes, [])

    def test_empty_string_is_noop(self):
        with patch('subprocess.check_output') as mock:
            result = self.fns.replace_rootfs_uuid(self.d, '')
        self.assertEqual(result, '')
        mock.assert_not_called()

    def test_replaces_syslinux_style_append(self):
        append = 'root=UUID=%s rw console=ttyS0,115200' % UUID_PLACEHOLDER
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            result = self.fns.replace_rootfs_uuid(self.d, append)
        self.assertEqual(result, 'root=UUID=%s rw console=ttyS0,115200' % SAMPLE_UUID)

    def test_replaces_grub_style_root_argument(self):
        root = 'root=UUID=%s' % UUID_PLACEHOLDER
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            result = self.fns.replace_rootfs_uuid(self.d, root)
        self.assertEqual(result, 'root=UUID=%s' % SAMPLE_UUID)

    def test_replaces_systemd_boot_options_line(self):
        # systemd-boot-cfg.bbclass writes: options LABEL=boot <APPEND>
        options = 'LABEL=boot root=UUID=%s quiet' % UUID_PLACEHOLDER
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            result = self.fns.replace_rootfs_uuid(self.d, options)
        self.assertEqual(result, 'LABEL=boot root=UUID=%s quiet' % SAMPLE_UUID)

    def test_replaces_every_placeholder(self):
        string = 'root=%s resume=%s' % (UUID_PLACEHOLDER, UUID_PLACEHOLDER)
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output) as mock:
            result = self.fns.replace_rootfs_uuid(self.d, string)
        self.assertEqual(result, 'root=%s resume=%s' % (SAMPLE_UUID, SAMPLE_UUID))
        self.assertEqual(mock.call_count, 1)

    def test_placeholder_only(self):
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            self.assertEqual(self.fns.replace_rootfs_uuid(self.d, UUID_PLACEHOLDER), SAMPLE_UUID)

    def test_near_miss_placeholders_are_left_alone(self):
        # Users must use the exact token documented in the class.
        for token in (
            '<uuid-of-rootfs>',
            '<<uuid-of-rootfs>',
            '<uuid-of-rootfs>>',
            '<<UUID-OF-ROOTFS>>',
            'uuid-of-rootfs',
        ):
            with patch('subprocess.check_output') as mock:
                result = self.fns.replace_rootfs_uuid(self.d, 'root=UUID=%s' % token)
            self.assertEqual(result, 'root=UUID=%s' % token, token)
            mock.assert_not_called()

    def test_placeholder_as_substring_is_replaced(self):
        # in/replace are substring matches, so an extra trailing '>' is kept.
        extra = UUID_PLACEHOLDER + '>'
        output = _tune2fs_output(SAMPLE_UUID)
        with patch('subprocess.check_output', return_value=output):
            result = self.fns.replace_rootfs_uuid(self.d, 'root=UUID=%s' % extra)
        self.assertEqual(result, 'root=UUID=%s>' % SAMPLE_UUID)

    def test_replacement_failure_when_uuid_cannot_be_read(self):
        with patch('subprocess.check_output', return_value=''):
            with self.assertRaises(BBFatal):
                self.fns.replace_rootfs_uuid(self.d, 'root=UUID=%s' % UUID_PLACEHOLDER)


@unittest.skipUnless(shutil.which('tune2fs'), 'tune2fs not installed')
@unittest.skipUnless(
    shutil.which('mkfs.ext4') or shutil.which('mkfs.ext3') or shutil.which('mkfs.ext2'),
    'no mkfs.ext* available',
)
class TestGetRootfsUuidLive(FsUuidTestCase):
    """Parser vs a real ext image. Skipped on hosts without e2fsprogs."""

    def _mkfs(self):
        for name in ('mkfs.ext4', 'mkfs.ext3', 'mkfs.ext2'):
            path = shutil.which(name)
            if path:
                return path
        self.fail('mkfs.ext* disappeared after skip check')

    def test_reads_uuid_from_real_ext_image(self):
        mkfs = self._mkfs()
        with tempfile.TemporaryDirectory(prefix='oe-fs-uuid-') as tmp:
            image = os.path.join(tmp, 'rootfs.img')
            with open(image, 'wb') as f:
                f.truncate(8 * 1024 * 1024)
            subprocess.check_call(
                [mkfs, '-F', '-q', image],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
            )
            dumped = subprocess.check_output(['tune2fs', '-l', image], text=True)
            expected = None
            for line in dumped.split('\n'):
                if line.startswith('Filesystem UUID:'):
                    expected = line.split()[-1]
                    break
            self.assertIsNotNone(expected, 'tune2fs -l did not print a UUID')

            uuid = self.fns.get_rootfs_uuid(FakeDataStore(image))
            self.assertEqual(uuid, expected)
            replaced = self.fns.replace_rootfs_uuid(
                FakeDataStore(image),
                'root=UUID=%s' % UUID_PLACEHOLDER,
            )
            self.assertEqual(replaced, 'root=UUID=%s' % expected)
