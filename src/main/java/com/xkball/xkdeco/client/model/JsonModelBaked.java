package com.xkball.xkdeco.client.model;

import com.github.bsideup.jabel.Desugar;
import com.xkball.xkdeco.client.render.Quad;

import java.util.List;

//todo 应用transformer
@Desugar
public record JsonModelBaked(List<Quad> quads) {
}
