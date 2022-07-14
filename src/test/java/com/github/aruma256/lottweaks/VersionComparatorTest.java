package com.github.aruma256.lottweaks;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class VersionComparatorTest {

	@Test
	final void testIsClientNewerOrTheSame() {
		// If client mod and server mod versions are the same, return true
		assertTrue(VersionComparator.isClientNewerOrTheSame("3.3.3", "3.3.3"));
		// If client mod is newer than server mod, return true
		assertTrue(VersionComparator.isClientNewerOrTheSame("3.3.3", "2.3.3"));
		assertTrue(VersionComparator.isClientNewerOrTheSame("3.3.3", "3.2.3"));
		assertTrue(VersionComparator.isClientNewerOrTheSame("3.3.3", "3.3.2"));
		assertTrue(VersionComparator.isClientNewerOrTheSame("3.3.3", "2.2.3a")); //NOTE: Some older mods have an alphabet at the end of the version
		// If client is older than the server, return false
		assertFalse(VersionComparator.isClientNewerOrTheSame("3.3.3", "4.3.3"));
		assertFalse(VersionComparator.isClientNewerOrTheSame("3.3.3", "3.4.3"));
		assertFalse(VersionComparator.isClientNewerOrTheSame("3.3.3", "3.3.4"));
		// Should not raise exceptions even if the version name is manipulated
		assertDoesNotThrow(() -> VersionComparator.isClientNewerOrTheSame("3.3.3", ""));
		assertDoesNotThrow(() -> VersionComparator.isClientNewerOrTheSame("3.3.3", "ZZZ"));
		assertDoesNotThrow(() -> VersionComparator.isClientNewerOrTheSame("3.3.3", "Z.Z."));
		assertDoesNotThrow(() -> VersionComparator.isClientNewerOrTheSame("3.3.3", "0.0.0.0"));
	}

}
