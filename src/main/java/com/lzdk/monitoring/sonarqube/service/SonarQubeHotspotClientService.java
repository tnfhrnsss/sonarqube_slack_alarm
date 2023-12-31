package com.lzdk.monitoring.sonarqube.service;

import java.util.List;

import com.lzdk.monitoring.sonarqube.client.SonarQubeHotspotClient;
import com.lzdk.monitoring.sonarqube.client.model.hotspot.HotspotList;
import com.lzdk.monitoring.sonarqube.client.model.hotspot.HotspotSearchQuery;
import com.lzdk.monitoring.sonarqube.client.model.hotspot.HotspotSearchResultRdo;
import com.lzdk.monitoring.sonarqube.utils.SonarqubeProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SonarQubeHotspotClientService {
    private final SonarQubeHotspotClient sonarQubeHotspotClient;

    private final SonarqubeProperties sonarqubeProperties;

    public HotspotList findAll() {
        HotspotList combineList = new HotspotList();
        try {
            HotspotSearchQuery.HotspotSearchQueryBuilder builder = HotspotSearchQuery.builder().build().toBuilder();
            sonarqubeProperties.getComponentKeys().forEach(
                key -> {
                    HotspotSearchResultRdo rdo = sonarQubeHotspotClient.search(builder.projectKey(key).build());
                    if (rdo.exist()) {
                        combineList.combine(rdo.getHotspots());
                    }
                }
            );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return combineList;
    }
}
